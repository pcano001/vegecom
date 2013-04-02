package com.veisite.vegecom.ui.tercero;

import java.util.List;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.ui.framework.component.table.AbstractListTableModel;
import com.veisite.vegecom.ui.service.TerceroUIService;

/**
 * Clase que implementa el modelo de tabla de un tercero. 
 * 
 * Un tercero puede ser un cliente o un proveedor y muestra los
 * datos generales en formato de tabla con las siguientes columnas:
 * 
 * 	0 - cif
 * 	1 - nombre
 * 	2 - telefono
 * 	3 - email
 * 	4 - provincia
 * 	5 - codigo postal
 * 	6 - localidad+municipio
 * 	7 - domicilio
 * 	
 * 
 * @author josemaria
 *
 */
public class TerceroListTableModel<T extends TerceroComercial> 
									extends AbstractListTableModel<T> {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 4478333403718532551L;

	private static Logger logger = LoggerFactory.getLogger(TerceroListTableModel.class);
	
	/**
	 * recursos graficos para cadenas de texto
	 * 
	 */
	private TerceroUIService<T> uiService;

	public TerceroListTableModel(TerceroListProvider<T> dataProvider, TerceroUIService<T> uiService) {
		super(dataProvider);
		Assert.notNull(uiService);
		this.uiService = uiService;
		logger.debug("Creating a Tercero List Table Model with dataProvider");
	}
	
	public TerceroListTableModel(List<T> dataList, TerceroUIService<T> uiService) {
		super(dataList);
		Assert.notNull(uiService);
		this.uiService = uiService;
		logger.debug("Creating a Tercero List Table Model with dataList");
	}
	
	public TerceroListTableModel(TerceroUIService<T> uiService) {
		super();
		Assert.notNull(uiService);
		this.uiService = uiService;
		logger.debug("Creating a Tercero List Table Model with empty data");
	}
	
	@Override
	public int getColumnCount() {
		return 8;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TerceroComercial item = getItemAt(rowIndex);
		if (item==null) return null;
		switch (columnIndex) {
		case 0:
			return item.getNif();
		case 1:
			return item.getNombre();
		case 2:
			return item.getTelefono();
		case 3:
			return item.getEmail();
		case 4:
			return item.getProvincia()==null ? null : item.getProvincia().getNombre();
		case 5:
			return item.getCodigoPostal();
		case 6:
			String l = "";
			if (item.getLocalidad()!=null) l = item.getLocalidad().trim();
			Municipio m = item.getMunicipio();
			if (m!=null) {
				if (!l.isEmpty()) l+=" ("+m.getNombre()+")";
				else l += m.getNombre();
			}
			return l;
		case 7:
			return item.getDomicilio();
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return uiService.getMessage("ui.tercero.TerceroListTable.cifLabel", null, "Nif");
		case 1:
			return uiService.getMessage("ui.tercero.TerceroListTable.nameLabel", null, "Nif");
		case 2:
			return uiService.getMessage("ui.tercero.TerceroListTable.telefonoLabel", null, "Nif");
		case 3:
			return uiService.getMessage("ui.tercero.TerceroListTable.emailLabel", null, "Nif");
		case 4:
			return uiService.getMessage("ui.tercero.TerceroListTable.provinciaLabel", null, "Nif");
		case 5:
			return uiService.getMessage("ui.tercero.TerceroListTable.cpostalLabel", null, "Nif");
		case 6:
			return uiService.getMessage("ui.tercero.TerceroListTable.municipioLabel", null, "Nif");
		case 7:
			return uiService.getMessage("ui.tercero.TerceroListTable.domicilioLabel", null, "Nif");
		}
		return super.getColumnName(column);
	}

	
	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex<getColumnCount()) return String.class;
		return super.getColumnClass(columnIndex);
	}

	@Override
	public void configureColumns(TableColumnModel columnModel) {
		int i=0;
		TableColumn col;
		col = columnModel.getColumn(i++);
		configureCol(col,80,85,null);
		col = columnModel.getColumn(i++);
		configureCol(col,150,250,null);
		col = columnModel.getColumn(i++);
		configureCol(col,50,120,null);
		col = columnModel.getColumn(i++);
		configureCol(col,100,120,null);
		col = columnModel.getColumn(i++);
		configureCol(col,80,100,null);
		col = columnModel.getColumn(i++);
		configureCol(col,40,40,null);
		col = columnModel.getColumn(i++);
		configureCol(col,80,120,null);
		col = columnModel.getColumn(i++);
		configureCol(col,80,150,null);
	}

	private void configureCol(TableColumn col, int minWidth, int preferredWidth, TableCellRenderer renderer) {
		col.setMinWidth(minWidth);
		col.setPreferredWidth(preferredWidth);
		if (renderer!=null) col.setCellRenderer(renderer);
	}
	
}
