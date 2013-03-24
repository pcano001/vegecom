package com.veisite.utils.binding;

public interface IBindableTo<SV> {

	public void addBindTo(BindTarget<SV> target);
	
	public void removeBindTo(BindTarget<SV> target);

}
