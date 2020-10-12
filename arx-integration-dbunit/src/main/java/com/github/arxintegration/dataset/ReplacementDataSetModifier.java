package com.github.arxintegration.dataset;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;

/**
 * {@link DataSetModifier} to create a {@link ReplacementDataSet}.
 *
 * @author Nenad Jevdjenic
 */
public abstract class ReplacementDataSetModifier implements DataSetModifier {

	public IDataSet modify(IDataSet dataSet) {
		if (!(dataSet instanceof ReplacementDataSet)) {
			dataSet = new ReplacementDataSet(dataSet);
		}
		addReplacements((ReplacementDataSet) dataSet);
		return dataSet;
	}

	protected abstract void addReplacements(ReplacementDataSet dataSet);

}
