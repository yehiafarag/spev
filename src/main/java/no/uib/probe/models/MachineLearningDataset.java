/*
 * Copyright (c) 2010-2021 Haifeng Li. All rights reserved.
 *
 * Smile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Smile.  If not, see <https://www.gnu.org/licenses/>.
 */
package no.uib.probe.models;

import java.io.Serializable;
import smile.data.type.DataType;
import smile.data.DataFrame;
import smile.data.formula.Formula;

/**
 *
 * @author Haifeng
 */
public class MachineLearningDataset implements Serializable {

    public DataFrame train;
    public Formula formula = Formula.lhs("Class");
    public double[][] x;
    public int[] y;
    public double[] y_reg;
    public String[] columnIds;

    public MachineLearningDataset(DataFrame dataframe, String[] columnIds, String[] deactivatedCols) {
        train = dataframe;
        this.columnIds = columnIds;
        for (String deactivatedCol : deactivatedCols) {
            train = train.drop(deactivatedCol);
            System.out.println("at deactivate column "+deactivatedCol);
        }

        train = train.drop(0).factorize("Class");      
        
        x = formula.x(train).toArray();
        y = formula.y(train).toIntArray();
        y_reg = formula.y(train).toDoubleArray();

    }

}
