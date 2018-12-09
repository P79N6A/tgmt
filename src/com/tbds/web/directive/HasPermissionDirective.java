package com.tbds.web.directive;

import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

public class HasPermissionDirective extends Directive {

	@Override
	public void exec(Env env, Scope scope, Writer out) {
		scope = new Scope(scope);
		//待实现
	}

}
