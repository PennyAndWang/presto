/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.prestosql.operator.aggregation;

import com.google.common.collect.ImmutableList;
import io.prestosql.spi.block.Block;
import io.prestosql.spi.type.Type;
import org.apache.commons.math3.stat.correlation.Covariance;

import java.util.List;

import static io.prestosql.block.BlockAssertions.createDoubleSequenceBlock;
import static io.prestosql.operator.aggregation.AggregationTestUtils.constructDoublePrimitiveArray;
import static io.prestosql.spi.type.DoubleType.DOUBLE;

public class TestDoubleCovariancePopAggregation
        extends AbstractTestAggregationFunction
{
    @Override
    protected Block[] getSequenceBlocks(int start, int length)
    {
        return new Block[] {createDoubleSequenceBlock(start, start + length), createDoubleSequenceBlock(start + 5, start + 5 + length)};
    }

    @Override
    protected String getFunctionName()
    {
        return "covar_pop";
    }

    @Override
    protected List<Type> getFunctionParameterTypes()
    {
        return ImmutableList.of(DOUBLE, DOUBLE);
    }

    @Override
    protected Object getExpectedValue(int start, int length)
    {
        if (length <= 0) {
            return null;
        }
        if (length == 1) {
            return 0.;
        }
        Covariance covariance = new Covariance();
        return covariance.covariance(constructDoublePrimitiveArray(start + 5, length), constructDoublePrimitiveArray(start, length), false);
    }
}
