package com.github.cg;

import com.github.cg.annotation.Plugin;
import com.github.cg.manager.CgPropertiesManager;
import com.github.cg.model.Patterns;

@Plugin(	
	patterns = { 
			Patterns.NONE,
			Patterns.CURRENCY,
			Patterns.PERCENT,
			Patterns.EMAIL,
			Patterns.SITE,
			Patterns.ZIPCODE,
			Patterns.PHONE
	},
	requiredProperties = {
			CgPropertiesManager.DIRS_SRC,
			CgPropertiesManager.DIRS_RESOURCES,
			CgPropertiesManager.DIRS_WEBCONTENT
	}
)
public interface CodeGeneratorPlugin {}
