package br.com.atos.cg;

import com.github.cg.annotation.Plugin;
import com.github.cg.model.Patterns;

@Plugin(
	patterns = { 
			Patterns.CURRENCY,
			Patterns.PERCENT,
			Patterns.EMAIL,
			Patterns.SITE,
			Patterns.ZIPCODE,
			Patterns.PHONE
	}
)
public interface CodeGeneratorPlugin {}
