/*******************************************************************************
 * Copyright (c) 2014 Open Door Logistics (www.opendoorlogistics.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at http://www.gnu.org/licenses/lgpl.txt
 ******************************************************************************/
package com.opendoorlogistics.components.reports;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opendoorlogistics.core.utils.ui.FileBrowserPanel;
import com.opendoorlogistics.core.utils.ui.ShowPanel;
import com.opendoorlogistics.core.utils.ui.TextEntryPanel;
import com.opendoorlogistics.core.utils.ui.TextEntryPanel.EntryType;
import com.opendoorlogistics.core.utils.ui.TextEntryPanel.TextChangedListener;
import com.opendoorlogistics.core.utils.ui.VerticalLayoutPanel;
import com.opendoorlogistics.api.ui.UIFactory.FilenameChangeListener;

final class ReporterConfigPanel extends VerticalLayoutPanel {
	final private ReporterConfig rc;
	final private FileBrowserPanel compiledReport;

	// Export options
	final private JCheckBox exportCheckBox;
	final private FileBrowserPanel exportDirectory;
	final private TextEntryPanel exportFilenamePrefix;

	// Export checkboxes...
	final private JCheckBox showViewerCheckbox;
	final private JCheckBox csv;
	final private JCheckBox docx;
	final private JCheckBox odt;
	final private JCheckBox html;
	final private JCheckBox pdf;
	final private JCheckBox xls;
	
	final private JCheckBox openFileAfterwords;

	private void updateEnabled() {
		exportDirectory.setEnabled(rc.isExportToFile());
		exportFilenamePrefix.setEnabled(rc.isExportToFile());
		csv.setEnabled(rc.isExportToFile());
		docx.setEnabled(rc.isExportToFile());
		odt.setEnabled(rc.isExportToFile());
		html.setEnabled(rc.isExportToFile());
		pdf.setEnabled(rc.isExportToFile());
		xls.setEnabled(rc.isExportToFile());
		openFileAfterwords.setEnabled(rc.isExportToFile());
	}

	ReporterConfigPanel(ReporterConfig rc){
		this.rc = rc;
		TextChangedListener listener = new TextChangedListener() {

			@Override
			public void textChange(String newText) {
				readFromPanel();
			}
		};
		ItemListener itemListener = new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				readFromPanel();
			}
		};

		FilenameChangeListener filenameListener = new FilenameChangeListener() {

			@Override
			public void filenameChanged(String newFilename) {
				readFromPanel();
			}
		};

		FileNameExtensionFilter jasperFilter = new FileNameExtensionFilter("Compile Jasper report   (.jasper)", "jasper");
		compiledReport = new FileBrowserPanel("Compiled report template (.jasper) ", rc.getCompiledReport(), filenameListener, false, "OK", jasperFilter);
		compiledReport.setToolTipText("Compiled reports can be generated by editing and then exporting the jrxml file in JasperReport's iReport application.");
		

		showViewerCheckbox = new JCheckBox("Show report viewer control", rc.isShowViewer());
		showViewerCheckbox.setBorder(null);
		showViewerCheckbox.addItemListener(itemListener);

		exportCheckBox = new JCheckBox("Export generated report to file", rc.isExportToFile());
		exportCheckBox.addItemListener(itemListener);
		exportCheckBox.setBorder(null);
		exportDirectory = new FileBrowserPanel("Export directory ", rc.getExportDirectory(), filenameListener, true, "OK");
		exportFilenamePrefix = new TextEntryPanel("Export filename prefix ", null, rc.getExportFilenamePrefix(), null, EntryType.String, listener);

		openFileAfterwords = new JCheckBox("Open file after exporting", rc.isOpenExportFile());
		openFileAfterwords.addItemListener(itemListener);
		
		csv = new JCheckBox("CSV    ", rc.isCsv());
		docx = new JCheckBox("Word    ", rc.isDocx());
		odt = new JCheckBox("OpenOffice    ", rc.isOdt());
		html = new JCheckBox("HTML    ", rc.isHtml());
		pdf = new JCheckBox("PDF    ", rc.isPdf());
		xls = new JCheckBox("Excel    ", rc.isXls());
		csv.addItemListener(itemListener);
		docx.addItemListener(itemListener);
		odt.addItemListener(itemListener);
		html.addItemListener(itemListener);
		pdf.addItemListener(itemListener);
		xls.addItemListener(itemListener);

		// have separate panel for generation options

		// add everything to the panel
		add( compiledReport);
		addWhitespace();
		add(showViewerCheckbox);
		addWhitespace();

		// create a separate panel for export options
		VerticalLayoutPanel exportPanel = new VerticalLayoutPanel();
		add(exportCheckBox);
		exportPanel.add( exportDirectory);
		exportPanel.addHalfWhitespace();
		exportPanel.addLine(openFileAfterwords,Box.createRigidArea(new Dimension(40, 2)),exportFilenamePrefix, Box.createGlue());
		exportPanel.addHalfWhitespace();
	//	csv.setBorder(null);
		exportPanel.addLine(csv, xls, html, odt, pdf, docx);
		exportPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
	//	exportPanel.setBorder(BorderFactory.createTitledBorder("Report export options"));
		add(exportPanel);

		updateEnabled();
	}

	private void readFromPanel() {
		rc.setCompiledReport(compiledReport.getFilename());

		rc.setOpenExportFile(openFileAfterwords.isSelected());

		rc.setExportToFile(exportCheckBox.isSelected());
		rc.setExportDirectory(exportDirectory.getFilename());
		rc.setExportFilenamePrefix(exportFilenamePrefix.getText());
		rc.setShowViewer(showViewerCheckbox.isSelected());
		rc.setCsv(csv.isSelected());
		rc.setDocx(docx.isSelected());
		rc.setOdt(odt.isSelected());
		rc.setHtml(html.isSelected());
		rc.setPdf(pdf.isSelected());
		rc.setXls(xls.isSelected());

		updateEnabled();
	}

	public static void main(String []args){
		ShowPanel.showPanel(new ReporterConfigPanel(new ReporterConfig()));
	}
}
