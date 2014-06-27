package org.wso2.developerstudio.datamapper.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.wso2.developerstudio.datamapper.diagram.part.DataMapperDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer {

	/**
	 * @generated
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = getPreferenceStore();
		DiagramGeneralPreferencePage.initDefaults(store);
		DiagramAppearancePreferencePage.initDefaults(store);
		DiagramConnectionsPreferencePage.initDefaults(store);
		DiagramPrintingPreferencePage.initDefaults(store);
		DiagramRulersAndGridPreferencePage.initDefaults(store);

	}

	/**
	 * @generated
	 */
	protected IPreferenceStore getPreferenceStore() {
		return DataMapperDiagramEditorPlugin.getInstance().getPreferenceStore();
	}
}
