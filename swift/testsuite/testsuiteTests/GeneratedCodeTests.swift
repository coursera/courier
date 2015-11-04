//
//  testsuiteTests.swift
//  testsuiteTests
//
//  Created by Joe Betz on 10/16/15.
//  Copyright © 2015 Joe Betz. All rights reserved.
//

import XCTest
import SwiftyJSON
import Foundation

@testable import testsuite

class GeneratedCodeTests: XCTestCase {
    // TODO(jbetz): Locate a xcode variable for the project root, use that instead of the /base/coursera path here.
    let jsonDir = NSProcessInfo.processInfo().environment["HOME"]! + "/base/coursera/courier/swift/generator/src/test/resources/test/records/"
    
    func testWithComplexTypesMap() {
        let json = try! jsonFile("WithComplexTypesMap.json");
        let deserialized = WithComplexTypesMap.read(json);
        
        let expected = WithComplexTypesMap(
            empties: ["a": Empty(), "b": Empty(), "c": Empty()],
            fruits: ["a": Fruits.APPLE, "b": Fruits.BANANA, "c": Fruits.ORANGE],
            arrays: ["a": [ Simple(message: "v1"), Simple(message: "v2")]],
            maps: ["o1": ["i1": Simple(message: "o1i1"), "i2": Simple(message: "o1i2")]],
            unions: ["a": .IntMember(1), "b": .StringMember("u1")],
            fixed: ["a": "\u{0000}\u{0001}\u{0002}\u{0003}\u{0004}\u{0005}\u{0006}\u{0007}"])
        
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithCustomTypesArray() {
        let json = try! jsonFile("WithCustomTypesArray.json");
        let deserialized = WithCustomTypesArray.read(json);
        
        let expected = WithCustomTypesArray(
            ints: [1, 2, 3],
            arrays: [[ Simple(message: "a1")]],
            maps: [["a": Simple(message: "m1")]],
            unions: [.IntMember(1), .StringMember("str"), .SimpleMember(Simple(message: "u1"))],
            fixed: [ "\u{0000}\u{0001}\u{0002}\u{0003}\u{0004}\u{0005}\u{0006}\u{0007}"])
        
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithCustomTypesMap() {
        let json = try! jsonFile("WithCustomTypesMap.json");
        let deserialized = WithCustomTypesMap.read(json);
        
        let expected = WithCustomTypesMap(ints: ["a": 1, "b": 2, "c": 3])
        
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithFlatTypedDefinition() {
        let json = try! jsonFile("WithFlatTypedDefinition.json");
        let deserialized = WithFlatTypedDefinition.read(json);
        
        let expected = WithFlatTypedDefinition(
            value: .MessageMember(Message(title: "title", body: "Hello, Swift.")))
        
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithPrimitives() {
        let json = try! jsonFile("WithPrimitives.json");
        let deserialized = WithPrimitives.read(json);
        
        let expected = WithPrimitives(intField: 1, longField: 2, floatField: 3.3, doubleField: 4.4, booleanField: true, stringField: "str", bytesField: "\u{0000}\u{0001}\u{0002}")
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithPrimitivesArray() {
        let json = try! jsonFile("WithPrimitivesArray.json");
        let deserialized = WithPrimitivesArray.read(json);
        
        let expected = WithPrimitivesArray(
            ints: [1, 2, 3],
            longs: [10, 20, 30],
            floats: [1.1, 2.2, 3.3],
            doubles: [11.1, 22.2, 33.3],
            booleans: [false, true],
            strings: ["a", "b", "c"],
            bytes: ["\u{0000}\u{0001}\u{0002}", "\u{0003}\u{0004}\u{0005}"])
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithPrimitivesMap() {
        let json = try! jsonFile("WithPrimitivesMap.json");
        let deserialized = WithPrimitivesMap.read(json);
        
        let expected = WithPrimitivesMap(
            ints: ["a": 1, "b": 2, "c": 3],
            longs: ["a": 10, "b": 20, "c": 30],
            floats: ["a": 1.1, "b": 2.2, "c": 3.3],
            doubles: ["a": 11.1, "b": 22.2, "c": 33.3],
            booleans: ["a": true, "b": false, "c": true],
            strings: ["a": "string1", "b": "string2", "c": "string3"],
            bytes: ["a": "\u{0000}\u{0001}\u{0002}", "b": "\u{0003}\u{0004}\u{0005}", "c": "\u{0006}\u{0007}\u{8}"])
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithRecordArray() {
        let json = try! jsonFile("WithRecordArray.json");
        let deserialized = WithRecordArray.read(json);
        
        let expected = WithRecordArray(
            empties: [Empty(), Empty(), Empty()],
            fruits: [Fruits.APPLE, Fruits.BANANA, Fruits.ORANGE])
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithTypedDefinition() {
        let json = try! jsonFile("WithTypedDefinition.json");
        let deserialized = WithTypedDefinition.read(json);
        
        let expected = WithTypedDefinition(
            value: .MessageMember(Message(title: "title", body: "Hello, Swift.")))
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithTypedKeyMap() {
        let json = try! jsonFile("WithTypedKeyMap.json");
        let deserialized = WithTypedKeyMap.read(json);
        
        // TODO: implement once we have typed key maps
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }
    
    func testWithUnion() {
        let json = try! jsonFile("WithUnion.json");
        let deserialized = WithUnion.read(json);
        
        let expected = WithUnion(value: .MessageMember(Message(title: "title", body: "Hello, Swift.")))
        XCTAssertEqual(deserialized, expected)
        
        let serialized = deserialized.write();
        assertSameJsObject(json.rawString()!, actual: serialized.rawString()!)
    }

    // TODO: This is lame.  I'd prefer use SwiftyJSON to do the comparison here as well, but was unable to 
    // figure out how to compare JSON correctly with it
    func assertSameJsObject(expected: String, actual: String) {
        //print("actual:" + actual)
        //print("expected:" + expected)

        let expectedData = expected.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!
        let expectedObj = try! NSJSONSerialization.JSONObjectWithData(expectedData, options: NSJSONReadingOptions.MutableContainers) as! NSDictionary
        
        let actualData = actual.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!
        let actualObj = try! NSJSONSerialization.JSONObjectWithData(actualData, options: NSJSONReadingOptions.MutableContainers) as! NSDictionary
        
        XCTAssertEqual(expectedObj, actualObj)
    }
    
    func jsonFile(name: String) throws -> JSON {
        print("directory: " + jsonDir)
        let jsonString = try! String(contentsOfFile: jsonDir + name)
        let dataFromString = jsonString.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!
        return JSON(data: dataFromString)
    }
}
