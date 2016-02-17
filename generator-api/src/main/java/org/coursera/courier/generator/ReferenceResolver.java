/*
 * Copyright 2016 Coursera Inc.
 *
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

package org.coursera.courier.generator;

import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.NamedDataSchema;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.pegasus.generator.spec.ArrayTemplateSpec;
import com.linkedin.pegasus.generator.spec.ClassTemplateSpec;
import com.linkedin.pegasus.generator.spec.RecordTemplateSpec;
import com.linkedin.pegasus.generator.spec.UnionTemplateSpec;
import org.coursera.courier.api.CourierMapTemplateSpec;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Ideally this should resolve references between Courier *files*.
 *
 * E.g. if a courier file contains a record declaration for A and the declaration also
 * contains a map[B, C], an array[D], a union[E, F] and a inline record G, then the directly
 * referenced types of A should be [B, C, D, E, F, G].
 *
 * But if any of the maps, arrays, or unions are declared in a separate Courier file (via
 * a typeref), e.g. A has a field of type Y, and Y is declared in it's own file as a typeref to
 * union[Z], then the referenced types of A should be [Y] and the referenced types of Y should be
 * [Z].
 *
 * Unfortunately, DataSchema does not track if something was declared in the same file or not. So this
 * might require making changes to pegasus.  So the temporary work around used here is to find all references
 * between named types, traversing unnamed types.
 */
public class ReferenceResolver {

  /**
   * Finds the first named types referenced by the given type in the schema tree.
   */
  public static Set<NamedDataSchema> referencedNamedTypes(ClassTemplateSpec spec) {
    Set<NamedDataSchema> results = new HashSet<>();
    if (spec instanceof RecordTemplateSpec) {
      RecordTemplateSpec recordSpec = (RecordTemplateSpec)spec;
      RecordDataSchema schema = recordSpec.getSchema();
      for (RecordTemplateSpec.Field field: recordSpec.getFields()) {
        results.addAll(findNearestNamedTypes(field.getType()));
        RecordDataSchema fieldRecord = field.getSchemaField().getRecord();
        if (fieldRecord != null && fieldRecord != schema) {
          // Found an "include" record. Add it.
          results.add(fieldRecord);
        }
      }
    } else if (spec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)spec;
      for (UnionTemplateSpec.Member member: unionSpec.getMembers()) {
        results.addAll(findNearestNamedTypes(member.getClassTemplateSpec()));
      }
    } else if (spec instanceof CourierMapTemplateSpec) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      if (mapSpec.getKeyClass() != null) {
        results.addAll(findNearestNamedTypes(mapSpec.getKeyClass()));
      }
      results.addAll(findNearestNamedTypes(mapSpec.getValueClass()));
    } else if (spec instanceof ArrayTemplateSpec) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      results.addAll(findNearestNamedTypes(arraySpec.getItemClass()));
    }
    return results;
  }

  /**
   * Finds the nearest NamedDataSchema types along all branches in the schema tree starting at the
   * root.
   *
   * If the root is a named type, it's NamedDataSchema will be returned, otherwise each type
   * referenced by the root will be treated as a branch and will be recursively searched for
   * the first NamedDataSchemas.  The resulting set of the nearest NamedDataSchemas along each branch
   * will be returned.
   */
  private static Set<NamedDataSchema> findNearestNamedTypes(ClassTemplateSpec spec) {
    if (spec.getSchema() instanceof NamedDataSchema) {
      return Collections.singleton((NamedDataSchema)spec.getSchema());
    }
    if (spec.getOriginalTyperefSchema() != null) {
      return Collections.<NamedDataSchema>singleton(spec.getOriginalTyperefSchema());
    }

    Set<NamedDataSchema> results = new HashSet<>();

    if (spec instanceof UnionTemplateSpec) {
      UnionTemplateSpec unionSpec = (UnionTemplateSpec)spec;
      for (UnionTemplateSpec.Member member: unionSpec.getMembers()) {
        results.addAll(findNearestNamedTypes(member.getClassTemplateSpec()));
      }
    } else if (spec instanceof CourierMapTemplateSpec) {
      CourierMapTemplateSpec mapSpec = (CourierMapTemplateSpec)spec;
      if (mapSpec.getKeyClass() != null) {
        results.addAll(findNearestNamedTypes(mapSpec.getKeyClass()));
      }
      results.addAll(findNearestNamedTypes(mapSpec.getValueClass()));
    } else if (spec instanceof ArrayTemplateSpec) {
      ArrayTemplateSpec arraySpec = (ArrayTemplateSpec)spec;
      results.addAll(findNearestNamedTypes(arraySpec.getItemClass()));
    }
    return results;
  }
}
