/*-
 * #%L
 * Elastic APM Java agent
 * %%
 * Copyright (C) 2018 the original author or authors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package co.elastic.apm.opentracing;

import co.elastic.apm.impl.ElasticApmTracer;
import io.opentracing.Scope;

class ApmScope implements Scope {

    private final boolean finishSpanOnClose;
    private final ApmSpan apmSpan;
    private final ElasticApmTracer tracer;

    ApmScope(boolean finishSpanOnClose, ApmSpan apmSpan, ElasticApmTracer tracer) {
        this.finishSpanOnClose = finishSpanOnClose;
        this.apmSpan = apmSpan;
        this.tracer = tracer;
    }

    @Override
    public void close() {
        if (finishSpanOnClose) {
            apmSpan.finish();
        }
        if (apmSpan.getTransaction() != null) {
            tracer.releaseActiveTransaction();
        } else if (apmSpan.getSpan() != null) {
            tracer.releaseActiveSpan();
        }
    }

    @Override
    public ApmSpan span() {
        return apmSpan;
    }
}
