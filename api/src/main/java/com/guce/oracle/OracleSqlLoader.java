package com.guce.oracle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author chengen.gce
 * @DATE 2022/2/11 9:02 PM
 */
public class OracleSqlLoader {

    /**
     * SQL*Loader process exit code. Operating-system independent.
     * The process itself returns an operating-system specific exit code, so we normalize it.
     * <p/>
     * See: https://docs.oracle.com/cd/B19306_01/server.102/b14215/ldr_params.htm#i1005019
     */
    public enum ExitCode {SUCCESS, FAIL, WARN, FATAL, UNKNOWN}

    /**
     * Return value from our high level API method.
     * It contains SQL*Loader process exit code File objects referring generated files.
     * This class is immutable, so no need for getters and setters.
     */
    public static class Results {
        public final ExitCode exitCode;
        public final File controlFile;
        public final File logFile;
        public final File badFile;
        public final File discardFile;

        public Results(ExitCode exitCode, File controlFile, File logFile, File badFile, File discardFile) {
            this.exitCode = exitCode;
            this.controlFile = controlFile;
            this.logFile = logFile;
            this.badFile = badFile;
            this.discardFile = discardFile;
        }
    }

    public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    /**
     * Helper method. Get list of table columns, to be inserted in control file.
     * TSV data file must match this column order.
     */
    public static List<String> getTableColumns(final Connection conn, final String tableName) throws SQLException {
        final List<String> ret = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("select COLUMN_NAME from USER_TAB_COLUMNS where TABLE_NAME = ? order by COLUMN_ID")) {
            ps.setObject(1, tableName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ret.add(rs.getString(1));
                }
            }
        }
        return ret;
    }

    /**
     * Helper method. Generate intermediate control file.
     */
    public static String createControlFile(
            final String dataFileName,
            final String badFileName,
            final String discardFileName,
            final String tableName,
            final List<String> columnNames
    ) {
        return "" +
                "load data infile '" + dataFileName + "'\n" +
                "badfile '" + badFileName + "'\n" +
                "discardfile '" + discardFileName + "'\n" +
                "append\n" +
                "into table " + tableName + "\n" +
                "fields terminated by '\\t'\n" + // TAB separated fields
                columnNames.toString().replace("[", "( ").replace("]", " )\n");
    }

    /**
     * Run SQL*Loader process.
     */
    public static ExitCode runSqlLdrProcess(
            final File initialDir,
            final String stdoutLogFile,
            final String stderrLogFile,
            final String controlFile,
            final String logFile,
            final String username,
            final String password,
            final String instance
    ) throws IOException {
        final ProcessBuilder pb = new ProcessBuilder(
                "sqlldr",
                "control=" + controlFile,
                "log=" + logFile,
                "userid=" + username + "/" + password + "@" + instance,
                "silent=header"
        );
        pb.directory(initialDir);
        if (stdoutLogFile != null) {
            pb.redirectOutput(ProcessBuilder.Redirect.appendTo(new File(initialDir, stdoutLogFile)));
        }
        if (stderrLogFile != null) {
            pb.redirectError(ProcessBuilder.Redirect.appendTo(new File(initialDir, stderrLogFile)));
        }
        final Process process = pb.start();
        try {
            process.waitFor(); // TODO may implement here timeout mechanism and progress monitor instead of just blocking the caller thread.
        } catch (InterruptedException ignored) {
        }

        final int exitCode = process.exitValue();

        // Exit codes are OS dependent. Convert them to our OS independent.
        // See: https://docs.oracle.com/cd/B19306_01/server.102/b14215/ldr_params.htm#i1005019
        switch (exitCode) {
            case 0:
                return ExitCode.SUCCESS;
            case 1:
                return ExitCode.FAIL;
            case 2:
                return ExitCode.WARN;
            case 3:
                return IS_WINDOWS ? ExitCode.FAIL : ExitCode.FATAL;
            case 4:
                return ExitCode.FATAL;
            default:
                return ExitCode.UNKNOWN;
        }
    }

    /**
     * High level API.
     * Wraps the logic of SQL*Loader tool.
     *
     * @param conn JDBC connection matching username, password and instance arguments. Used to read the column list of the table.
     * @param username to be fed to SQL*Loader process, should match JDBC connection details.
     * @param password to be fed to SQL*Loader process, should match JDBC connection details.
     * @param instance to be fed to SQL*Loader process, should match JDBC connection details.
     * @param tableName table to be populated
     * @param dataFile
     *      tab-separated values file to be inserted to the table.
     *      Column order must match table's column order.
     *      Check by running this SQL command:
     *      <pre>
     *      select * from USER_TAB_COLUMNS where table_name = '[your-table-name]' order by COLUMN_ID
     *      </pre>
     */
    public static Results bulkLoad(
            final Connection conn,
            final String username,
            final String password,
            final String instance,
            final String tableName,
            final File dataFile
    ) throws IOException, SQLException {
        final File initialDirectory = dataFile.getParentFile();
        final String dataFileName = dataFile.getName();
        final String controlFileName = dataFileName + ".ctl";
        final String logFileName = dataFileName + ".log";
        final String badFileName = dataFileName + ".bad";
        final String discardFileName = dataFileName + ".discard";

        final File controlFile = new File(initialDirectory, controlFileName);
        final List<String> columnNames = getTableColumns(conn, tableName);
        final String controlFileContents = createControlFile(dataFileName, badFileName, discardFileName, tableName, columnNames);
        Files.write(controlFile.toPath(), controlFileContents.getBytes(), StandardOpenOption.CREATE_NEW);

        final ExitCode exitCode = runSqlLdrProcess(
                initialDirectory,
                dataFileName + ".stdout.log",
                dataFileName + ".stderr.log",
                controlFileName,
                logFileName,
                username,
                password,
                instance
        );

        // Return to the caller names of files generated inside this method.
        Results ret = new Results(
                exitCode,
                controlFile,
                new File(initialDirectory, logFileName),
                new File(initialDirectory, badFileName),
                new File(initialDirectory, discardFileName)
        );
        return ret;
    }

    // TODO may add method to parse log file if required

    /**
     * Manual test method.
     */
    public static void main(String... args) throws Exception {

        // ========================================================================================================
        // Prepare connection
        // ========================================================================================================
        DriverManager.registerDriver((Driver) (Class.forName("oracle.jdbc.driver.OracleDriver").newInstance()));
        final String instance = System.getProperty("instance");
        final String username = System.getProperty("username");
        final String password = System.getProperty("password");
        final Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@" + instance, username, password);

        // ========================================================================================================
        // Run
        // ========================================================================================================
        final File dataFile = new File(args[1]);
        final String tableName = args[0];
        final Results results = bulkLoad(conn, username, password, instance, tableName, dataFile);

        // ========================================================================================================
        // Analyze
        // ========================================================================================================
        if (results.exitCode != ExitCode.SUCCESS) {
            System.err.println("Failed. Exit code: " + results.exitCode + ". See log file: " + results.logFile);
            System.exit(1);
        }
    }
}
