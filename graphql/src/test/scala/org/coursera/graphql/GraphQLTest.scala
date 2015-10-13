/*
 Copyright 2015 Coursera Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.coursera.graphql

import org.coursera.graphql.GraphQL.Parser.Alias
import org.coursera.graphql.GraphQL.Parser.Argument
import org.coursera.graphql.GraphQL.Parser.Arguments
import org.coursera.graphql.GraphQL.Parser.Document
import org.coursera.graphql.GraphQL.Parser.Field
import org.coursera.graphql.GraphQL.Parser.IntValue
import org.coursera.graphql.GraphQL.Parser.ListValue
import org.coursera.graphql.GraphQL.Parser.Name
import org.coursera.graphql.GraphQL.Parser.ObjectField
import org.coursera.graphql.GraphQL.Parser.ObjectValue
import org.coursera.graphql.GraphQL.Parser.OperationDefinition_SelectionSet
import org.coursera.graphql.GraphQL.Parser.SelectionSet
import org.coursera.graphql.GraphQL.Parser.StringValue
import org.coursera.graphql.GraphQL.Parser.Variable
import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit

class GraphQLTest extends AssertionsForJUnit {
  @Test
  def ints(): Unit = {
    assertMatch(GraphQL.Parser.intParser, ("0", 0), ("-0", 0), ("10", 10), ("-10", -10))
  }

  @Test
  def floats(): Unit = {
    assertMatch(GraphQL.Parser.floatParser, ("1", 1.0f), ("1.0", 1.0f), ("1.0e+10", 1.0e+10f))
  }

  @Test
  def booleans(): Unit = {
    assertMatch(GraphQL.Parser.booleanParser, ("true", true))
  }

  @Test
  def strings(): Unit = {
    assertMatch(GraphQL.Parser.stringParser, ("\"xyz\"", "xyz"))
  }

  @Test
  def list(): Unit = {
    val oneTwo = ListValue(Seq(IntValue(1), IntValue(2)))
    assertMatch(GraphQL.Parser.listValueParser, ("[1 2]", oneTwo))

    val strings = ListValue(Seq(StringValue("abc"), StringValue("def")))
    assertMatch(GraphQL.Parser.listValueParser, ("[\"abc\" \"def\"]", strings))
  }

  @Test
  def obj(): Unit = {
    assertMatch(
      GraphQL.Parser.objectValueParser, ("{ name: 2 }",
        ObjectValue(
          Seq(ObjectField(Name("name"), IntValue(2))))))
  }

  @Test
  def variable(): Unit = {
    assertMatch(GraphQL.Parser.variableParser, ("$x", Variable(Name("x"))))
  }

  @Test
  def selectionSet(): Unit = {
    val selectionSetInput = """ { id firstName lastName } """.stripMargin
    val selectionSet = SelectionSet(
      Seq(
        Field(name = Name("id")),
        Field(name = Name("firstName")),
        Field(name = Name("lastName"))))
    assertMatch(GraphQL.Parser.selectionSetParser, (selectionSetInput, selectionSet))
  }

  @Test
  def withAliases(): Unit = {
    val query = """
      |{
      |  user(id: 4) {
      |    id
      |    name
      |    smallPic: profilePic(size: 64)
      |    bigPic: profilePic(size: 1024)
      |  }
      |}
    """.stripMargin

    val selectionSet = SelectionSet(
      Seq(
        Field(
          name = Name("user"),
          arguments = Some(Arguments(Seq(Argument(Name("id"), IntValue(4))))),
          selectionSet = Some(SelectionSet(
            Seq(
              Field(name = Name("id")),
              Field(name = Name("name")),
              Field(
                alias = Some(Alias(Name("smallPic"))),
                name = Name("profilePic"),
                arguments = Some(Arguments(Seq(Argument(Name("size"), IntValue(64)))))),
              Field(
                alias = Some(Alias(Name("bigPic"))),
                name = Name("profilePic"),
                arguments = Some(Arguments(Seq(Argument(Name("size"), IntValue(1024))))))))))))

    val document = Document(Seq(OperationDefinition_SelectionSet(selectionSet)))

    assertMatch(GraphQL.Parser.documentParser, (query, document))
  }

  @Test
  def noFragments(): Unit = {
    val query = """
      |query noFragments {
      |  user(id: 4) {
      |    friends(first: 10) {
      |      id
      |      name
      |      profilePic(size: 50)
      |    }
      |    mutualFriends(first: 10) {
      |      id
      |      name
      |      profilePic(size: 50)
      |    }
      |  }
      |}
    """.stripMargin
    assertParses(GraphQL.Parser.documentParser, query)
  }

  @Test
  def withFragments(): Unit = {
    val query =
      """
        |query withFragments {
        |  user(id: 4) {
        |    friends(first: 10) {
        |      ...friendFields
        |    }
        |    mutualFriends(first: 10) {
        |      ...friendFields
        |    }
        |  }
        |}
        |
        |fragment friendFields on User {
        |  id
        |  name
        |  profilePic(size: 50)
        |}
      """.stripMargin
    assertParses(GraphQL.Parser.documentParser, query)
  }

  @Test
  def withNestedFragments(): Unit = {
    val query =
    """
      |query withNestedFragments {
      |  user(id: 4) {
      |    friends(first: 10) {
      |      ...friendFields
      |    }
      |    mutualFriends(first: 10) {
      |      ...friendFields
      |    }
      |  }
      |}
      |
      |fragment friendFields on User {
      |  id
      |  name
      |  ...standardProfilePic
      |}
      |
      |fragment standardProfilePic on User {
      |  profilePic(size: 50)
      |}
    """.stripMargin
    assertParses(GraphQL.Parser.documentParser, query)
  }

  @Test
  def withFragmentTypeing(): Unit = {
    val query =
      """
        |query FragmentTyping {
        |  profiles(handles: ["zuck", "cocacola"]) {
        |    handle
        |    ...userFragment
        |    ...pageFragment
        |  }
        |}
        |
        |fragment userFragment on User {
        |  friends {
        |    count
        |  }
        |}
        |
        |fragment pageFragment on Page {
        |  likers {
        |    count
        |  }
        |}
      """.stripMargin
    assertParses(GraphQL.Parser.documentParser, query)
  }

  @Test
  def withExpandedInfo(): Unit = {
    val query =
      """
        |query inlineFragmentNoType($expandedInfo: Boolean) {
        |  user(handle: "zuck") {
        |    id
        |    name
        |    ... @include(if: $expandedInfo) {
        |      firstName
        |      lastName
        |      birthday
        |    }
        |  }
        |}
      """.stripMargin
    assertParses(GraphQL.Parser.documentParser, query)
  }

  @Test
  def withInlineFragmentTyping(): Unit = {
    val query =
      """
        |query inlineFragmentTyping {
        |  profiles(handles: ["zuck", "cocacola"]) {
        |    handle
        |    ... on User {
        |      friends {
        |        count
        |      }
        |    }
        |    ... on Page {
        |      likers {
        |        count
        |      }
        |    }
        |  }
        |}
      """.stripMargin
    assertParses(GraphQL.Parser.documentParser, query)
  }


  def assertMatch[T](parser: GraphQL.Parser.Parser[T], cases: (String, Any)*): Unit = {
    cases.foreach { case (input, expected) =>
      val result = GraphQL.Parser.partial(input, parser)
      assert(expected === result)
    }
  }

  def assertParses[T](parser: GraphQL.Parser.Parser[T], input: String): Unit = {
    println(GraphQL.Parser.partial(input, parser))
  }
}
