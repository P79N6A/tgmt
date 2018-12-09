/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tbds.ctrl;

import com.jfinal.core.Controller;

/**
 *
 * @author totan
 */
public class HomeController extends TbdsBaseController {
    public void index() {
        render("index.html");
    }
    public void home() {
        render("index.html");
    }
}
