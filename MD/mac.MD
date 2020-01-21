1、mac下默认是没有 tree命令的，不过我们可以使用find命令模拟出tree命令的效果，如显示当前目录的 tree 的命令：


    find . -print | sed -e 's;[^/]*/;|____;g;s;____|; |;g'

2、当然你也可以写一个别名来快速执行该命令，运行如下命令，将上面这个命令写到~/.bash_profile里，以后直接运行tree命令就更方便了:


    alias tree="find . -print | sed -e 's;[^/]*/;|____;g;s;____|; |;g'"
