package com.guce.grvy.service.impl

import com.guce.grvy.service.IGrvy

/**
 * @Author chengen.gu* @DATE 2020/1/19 11:43 下午
 *
 *
 */
 class GrvyImpl implements IGrvy{

     @Override
     String printGrvy(String name) {
         return "hello " + name ;
     }
 }
