package com.veisite.vegecom.ui.framework.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.prompt.BuddySupport;

import com.veisite.utils.binding.BindTarget;
import com.veisite.utils.binding.IBindableTo;
import com.veisite.vegecom.ui.framework.ResourcesUtil;
import com.veisite.vegecom.ui.framework.component.util.IValidatableComponent;


public abstract class VSearchableTextField<T> extends JXTextField 
			implements IActivableComponent, IValidatableComponent, IBindableTo<T> {

    /**
	 * serial
	 */
	private static final long serialVersionUID = 7049363663302414133L;
	
	/**
	 * binding object to a Object property on this target
	 */
	List<BindTarget<T>> bindList = new ArrayList<BindTarget<T>>();
	
	/**
	 * Implementación de las validaciones
	 */
	private Validator validator;
	/**
	 * Objeto a validar
	 */
	private Object validatableObject;
	/**
	 * Propiedad a validar
	 */
	private String validatableProperty;
	/**
	 * flag de activacion de validaciones
	 */
	private boolean validationEnabled=false;
	/**
	 * Componente para mostrar errores de validacion
	 */
	private JLabel validationBuddy;
	
	/**
	 * Imagen de icono
	 */
	private static ImageIcon buddyIcon = null;
	
	/**
     * TIMEOUT es el tiempo que se espera a que no se pulse una tecla 
     * para lanzar la busqueda
     */
    private static final long TIMEOUT=500L;
   /**
     * Timer que ejecutas la tarea de filtro
     */
    private Timer searchTimer;
    /**
     * Tarea que se planifica para ejecutar el filtro
     */
    private TimerTask searchTimerTask=null;
    /**
     * Objeto para controlar el acceso al timer
     */
    private final Object lock = new Object();
    
    /**
     * Default and wait cursor
     */
    private Cursor defaultCursor;
    private Cursor waitCursor;
    
    /**
     * Object actual del componente
     * 	bound property
     */
    private T selectedObject = null;
    
    /**
     * Lista de elementos a mostrar en popup
     */
    private DefaultListModel<T> listModel;
    private JList<T> jlist;
    private JPanel popupPanel;
    
    /**
     * Popup para opciones
     */
    private Popup popup;
    
    /**
     * Controla si se esta actualizando la lista
     * para no propagar eventos de seleccion
     */
    private boolean updatingList = false;

    /**
     * Controla si se esta actualizando el texto
     * de otra forma a las pulsaciones de teclado
     * 
     */
    private boolean updatingText = false;
	
	
	public VSearchableTextField(int columns) {
		super();
		setColumns(columns);
		searchTimer = new Timer();
		init();
	}
	
	private void init() {
		defaultCursor = getCursor();
		waitCursor = new Cursor(Cursor.WAIT_CURSOR);
		// Hay que añadir un listener para buscar contenidos.
		getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!updatingText) scheduleTextChanged();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!updatingText) scheduleTextChanged();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!updatingText) scheduleTextChanged();
			}
		});
		// Añadir listener para la pérdida del foco
		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				exitField();
			}
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		// Crea el componente a incluir en el popup.
		popupPanel = new JPanel(new BorderLayout()); 
		listModel = new DefaultListModel<T>();
		jlist = new JList<T>(listModel);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setCellRenderer(new MyListCellRenderer());
		jlist.setBackground(popupPanel.getBackground());
		jlist.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (updatingList) return;
				if (e.getFirstIndex()<0 || e.getFirstIndex()>=listModel.getSize())
					objectSelected(null);
				objectSelected(listModel.get(e.getFirstIndex()));
			}
		});
		JScrollPane listScrollPane = new JScrollPane(jlist);
		popupPanel.add(listScrollPane,BorderLayout.CENTER);
		ImageIcon im = getBuddyIcon();
		if (im!=null) validationBuddy = new JLabel(im);
	}
	
	private static ImageIcon getBuddyIcon() {
		if (buddyIcon==null) {
			String path = (VTextField.class.getPackage().getName()).replace('.', '/');
			String resource = path+"/images/emblem-important-316.png";
			URL url = ResourcesUtil.getResource(resource);
			if (url!=null) buddyIcon = new ImageIcon(url);
		}
		return buddyIcon;
	}

	
	protected abstract List<T> getObjectList(String filter);

	
	/**
	 * Devuelve true si text es una cadena de texto cuyo contenido
	 * se puede usar para buscar los objetos necesarios para la lista
	 * false en otro caso.
	 * 
	 *   Las sibclases pueden sobreescribir el metodo para ajustar 
	 *   su funcionalidad.
	 *   
	 *   Po defecto requiere un text minimo de tres caracteres para 
	 *   iniciar la busqueda
	 * 
	 * @param text
	 * @return
	 */
	protected boolean isSearchable(String text)  {
		if (text.length()>=3) return true;
		return false;
	}
	
	/**
	 * Devuelve la representacion en cadena de caracteres del objeto.
	 * Para mostrarla en el campo de texto.
	 * 
	 * @param object
	 * @return
	 */
	protected String getObjectAsString(T object) {
		return object==null? null : object.toString();
	}
	
	
	private void objectSelected(T object) {
		hidePopup();
		setSelectedObject(object);
		transferFocus();
	}
	
	
	public T getSelectedObject() {
		return selectedObject;
	}
	
	public void setSelectedObject(T selectedObject) {
		firePropertyChange("selectedObject", this.selectedObject, this.selectedObject=selectedObject);
		setText(getObjectAsString(selectedObject));
		for (BindTarget<T> b : bindList) b.setValue(selectedObject);
		doValidation();
	}
	
	/**
	 * Metodo llamado cuando el timer avisa de que se podría
	 * lanzar una busqueda en función de las pulsaciones en
	 * el cuadro de texto. 
	 */
	private void fireSearch() {
		// Tomamos el texto sin espacio
		String t = getText().trim();
		// Preguntamos si es una cadena candidata a hacer una busqueda
		if (isSearchable(t)) {
			// Buscamos lista de objetos candidatos
			setCursor(waitCursor);
			List<T> lista = getObjectList(t);
			setCursor(defaultCursor);
			updateList(lista);
			showPopup();
		} else {
			hidePopup();
		}
	}

	
	private void showPopup() {
		jlist.setEnabled(true);
		if (popup!=null) return;
		// Establecer el tamaño y la posición.
		int w = getSize().width;
		int h = 200;
		popupPanel.setPreferredSize(new Dimension(w, h));
		// Calcular donde colocar el popup
		Point p = getLocation();
		Point ap = new Point(p);
		int th = getHeight();
		SwingUtilities.convertPointToScreen(ap,this);
		Dimension sS = Toolkit.getDefaultToolkit().getScreenSize();
		// Var si se puede poner debajo.
		if (ap.y+th+h <= sS.height-35) {
			ap.y = ap.y+th-5;
		} else {
			// Hay que ponerlo encima
			ap.y = ap.y-h-5;
		}
		ap.x -= 5;
		//SwingUtilities.convertPointFromScreen(ap,this);
		popup = PopupFactory.getSharedInstance().getPopup(this, popupPanel, ap.x, ap.y);
		popup.show();
	}
	
	private void hidePopup() {
		if (popup!=null) {
			popup.hide();
			popup=null;
		}
	}

	
	@Override
	public void setText(String t) {
		updatingText = true;
		super.setText(t);
		updatingText = false;
	}

	private void updateList(List<T> lista) {
		updatingList=true;
		listModel.clear();
		for (T o : lista) listModel.addElement(o);
		updatingList=false;
	}
	
	private boolean isObjectSyncronized() {
		return (selectedObject!=null && getText().trim().equals(getObjectAsString(selectedObject)));
	}
	
	
	/*
	 * Se quiere salir del campo editable.
	 *   Si el texto no coincide con el elemento seleccionado
	 *   ponerlo a null
	 */
	private void exitField() {
		if (selectedObject!=null && 
				!getText().trim().equals(getObjectAsString(selectedObject))) {
			setSelectedObject(null);
		} else setText(getObjectAsString(selectedObject)); 
		hidePopup();
	}
	
	/**
	 * Planifica para el timeput fijado el lanzamiento de la tarea
	 * de filtrado de filas
	 */
	private void scheduleTextChanged() {
		synchronized(lock) {
			cancelTimers();
			if (isObjectSyncronized()) return;
			jlist.setEnabled(false);
			searchTimerTask = new FilterTimerTask();
			searchTimer.schedule(searchTimerTask, TIMEOUT);
		}
	}
	
	private void cancelTimers() {
		if (searchTimerTask!=null) {
			searchTimerTask.cancel();
		}
		searchTimer.purge();
	}
	
	private class FilterTimerTask extends TimerTask {
		@Override
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					synchronized(lock) {
						cancelTimers();
						fireSearch();
					}
				}
			});
		}
	}
	
	
	private class MyListCellRenderer extends DefaultListCellRenderer {
		/**
		 * serial
		 */
		private static final long serialVersionUID = -2929713955384803124L;

		@Override
		public Component getListCellRendererComponent(JList<?> list,
				Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (c instanceof JLabel) {
				String t = getObjectAsString(listModel.get(index));
				((JLabel)c).setText(t);
			}
			return c;
		}
	}


	@Override
	public void addBindTo(BindTarget<T> target) {
		if (target==null) return;
		bindList.add(target);
	}

	public void removeBindTo(BindTarget<T> target) {
		if (target==null) return;
		bindList.remove(target);
	}


	@Override
	public void configureValidation(Validator validator, Object target,
			String property) {
		this.validator = validator;
		this.validatableObject = target;
		this.validatableProperty = property;
		enableValidation(true);
		doValidation();
	}

	@Override
	public void enableValidation(boolean validationEnabled) {
		this.validationEnabled=validationEnabled;
	}

	@Override
	public boolean isValidationEnabled() {
		return validationEnabled;
	}
	
	/**
	 * Metodo que realiza las validaciones
	 */
	private void doValidation() {
		if (!isValidationEnabled()) return;
		if (validator==null || validatableObject==null || validatableProperty==null) return;
		Set<ConstraintViolation<Object>> cv =
			validator.validateProperty(validatableObject,validatableProperty);
		if (cv.size()==0) {
			setToolTipText(getPrompt());
			removeAllBuddies();
		} else {
			Iterator<ConstraintViolation<Object>> it = cv.iterator();
			ConstraintViolation<Object> o = it.next();
			setToolTipText(o.getMessage());
			List<Component> lb = getBuddies(BuddySupport.Position.RIGHT);
			if (lb.contains(validationBuddy)) return;
			else {
				if (validationBuddy!=null) 
					addBuddy(validationBuddy, BuddySupport.Position.RIGHT);
			}
		}
	}
	
	/**
	 * Simulate a property to enable/disable component
	 */
	@Override
	public Boolean getActivado() {
		return new Boolean(super.isEnabled());
	}

	@Override
	public void setActivado(Boolean newActivado) {
		Boolean oldValue = getActivado();
		boolean b = (newActivado!=null && newActivado.booleanValue());
		super.setEnabled(b);
		firePropertyChange("activado", oldValue, getActivado());
	}
	
}
