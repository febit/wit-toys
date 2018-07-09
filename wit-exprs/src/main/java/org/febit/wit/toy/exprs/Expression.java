/*
 * Copyright 2018 febit.org.
 *
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
 */
package org.febit.wit.toy.exprs;

import java.util.Map;
import java.util.Objects;
import org.febit.wit.Context;
import org.febit.wit.Engine;
import org.febit.wit.Template;
import org.febit.wit.exceptions.ResourceNotFoundException;

/**
 *
 * @author zqq
 */
public class Expression {

  // 配置文件路径
  private final static String WIT_PROPS = "wit-exprs.wim";
  // 路由到表达式 Loader 所需的代码前缀
  private final static String LOADER_PREFIX = "expr: ";
  // 暂存表达式值的变量名
  private final static String INTERNAL_RESULT_VAR = "$$__result";

  /**
   * Engine Lazy Holder
   */
  private static final class EngineHolder {

    final static Engine ENGINE;

    static {
      ENGINE = Engine.create(WIT_PROPS);
    }
  }

  public static Engine getEngine() {
    return EngineHolder.ENGINE;
  }

  public static Expression create(String expr) {
    Template template;
    try {
      template = getEngine().getTemplate(buildSource(expr));
    } catch (ResourceNotFoundException ex) {
      throw new RuntimeException(ex);
    }
    return new Expression(template);
  }

  private static String buildSource(String expr) {
    Objects.requireNonNull(expr);
    return LOADER_PREFIX + INTERNAL_RESULT_VAR + " = " + expr;
  }

  private final Template template;

  private Expression(Template template) {
    this.template = template;
  }

  /**
   * Reload this Expression.
   */
  public void reload() {
    this.template.reload();
  }

  public Object invoke(Map<String, Object> params) {
    Context context = this.template.merge(params);
    return context.get(INTERNAL_RESULT_VAR);
  }

}
