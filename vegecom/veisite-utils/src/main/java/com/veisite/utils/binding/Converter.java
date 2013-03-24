package com.veisite.utils.binding;

public interface Converter<ST, DT> {
	public DT convert(ST value);
}
