package com.veisite.vegecom.ui.framework.component.table.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.RowFilter;

import org.jdesktop.swingx.sort.RowFilters.GeneralFilter;

import com.veisite.utils.StringUtil;

public class NoAcentosRegexFilter  extends GeneralFilter {

	private Matcher matcher;
	private boolean uppercase;
	
    @SuppressWarnings("unchecked")
	public static <M,I> RowFilter<M,I> createNoAcentosFilter(boolean uppercase, String regex,
            int... indices) {
    	return (RowFilter<M,I>)
    			new NoAcentosRegexFilter(uppercase, Pattern.compile(regex),indices);
    }

	public NoAcentosRegexFilter(boolean uppercase, Pattern regex, int... columns) {
		super(columns);
		this.uppercase = uppercase;
		if (regex == null) {
			throw new IllegalArgumentException("Pattern must be non-null");
		}
		matcher = regex.matcher("");
	}

	@Override
	protected boolean include(Entry<? extends Object, ? extends Object> value,
			int index) {
		String c = value.getStringValue(index);
		if (uppercase) c = c.toUpperCase();
		matcher.reset(StringUtil.quitaAcentos(c));
		return matcher.find();
	}

}
