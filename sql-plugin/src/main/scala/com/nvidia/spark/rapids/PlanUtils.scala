/*
 * Copyright (c) 2021, NVIDIA CORPORATION.
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

package com.nvidia.spark.rapids

import scala.util.Try

import org.apache.spark.sql.execution.SparkPlan

object PlanUtils {
  def getBaseNameFromClass(planClassStr: String): String = {
    val firstDotIndex = planClassStr.lastIndexOf(".")
    if (firstDotIndex != -1) planClassStr.substring(firstDotIndex + 1) else planClassStr
  }

  /**
   * Determines if plan is either fallbackCpuClass or a subclass thereof
   *
   * Useful subclass expression are LeafLike
   *
   * @param plan
   * @param fallbackCpuClass
   * @return
   */
  def sameClass(plan: SparkPlan, fallbackCpuClass: String): Boolean = {
    val planClass = plan.getClass
    val execNameWithoutPackage = getBaseNameFromClass(planClass.getName)
    execNameWithoutPackage == fallbackCpuClass ||
      plan.getClass.getName == fallbackCpuClass ||
      Try(java.lang.Class.forName(fallbackCpuClass))
        .map(_.isAssignableFrom(planClass))
        .getOrElse(false)
  }
}
