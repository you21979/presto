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
package com.facebook.presto.sql.planner.plan;

import com.facebook.presto.spi.OutputTableHandle;
import com.facebook.presto.sql.planner.Symbol;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public class TableCommitNode
        extends PlanNode
{
    private final PlanNode source;
    private final OutputTableHandle target;
    private final List<Symbol> outputs;

    @JsonCreator
    public TableCommitNode(
            @JsonProperty("id") PlanNodeId id,
            @JsonProperty("source") PlanNode source,
            @JsonProperty("target") OutputTableHandle target,
            @JsonProperty("outputs") List<Symbol> outputs)
    {
        super(id);

        checkArgument(target != null || source instanceof TableWriterNode);
        this.source = checkNotNull(source, "source is null");
        this.target = target;
        this.outputs = ImmutableList.copyOf(checkNotNull(outputs, "outputs is null"));
    }

    @JsonProperty
    public PlanNode getSource()
    {
        return source;
    }

    @JsonProperty
    public OutputTableHandle getTarget()
    {
        return target;
    }

    @JsonProperty("outputs")
    @Override
    public List<Symbol> getOutputSymbols()
    {
        return outputs;
    }

    @Override
    public List<PlanNode> getSources()
    {
        return ImmutableList.of(source);
    }

    @Override
    public <C, R> R accept(PlanVisitor<C, R> visitor, C context)
    {
        return visitor.visitTableCommit(this, context);
    }
}
