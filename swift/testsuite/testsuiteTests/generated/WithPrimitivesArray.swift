import Foundation
import SwiftyJSON

public struct WithPrimitivesArray: Serializable, Equatable {
    
    public let ints: [Int]?
    
    public let longs: [Int]?
    
    public let floats: [Float]?
    
    public let doubles: [Double]?
    
    public let booleans: [Bool]?
    
    public let strings: [String]?
    
    public let bytes: [String]?
    
    public init(
        ints: [Int]?,
        longs: [Int]?,
        floats: [Float]?,
        doubles: [Double]?,
        booleans: [Bool]?,
        strings: [String]?,
        bytes: [String]?
    ) {
        self.ints = ints
        self.longs = longs
        self.floats = floats
        self.doubles = doubles
        self.booleans = booleans
        self.strings = strings
        self.bytes = bytes
    }
    
    public static func readJSON(json: JSON) throws -> WithPrimitivesArray {
        return WithPrimitivesArray(
            ints: try json["ints"].optional(.Array).array.map {try $0.map { try $0.required(.Number).intValue } },
            longs: try json["longs"].optional(.Array).array.map {try $0.map { try $0.required(.Number).intValue } },
            floats: try json["floats"].optional(.Array).array.map {try $0.map { try $0.required(.Number).floatValue } },
            doubles: try json["doubles"].optional(.Array).array.map {try $0.map { try $0.required(.Number).doubleValue } },
            booleans: try json["booleans"].optional(.Array).array.map {try $0.map { try $0.required(.Bool).boolValue } },
            strings: try json["strings"].optional(.Array).array.map {try $0.map { try $0.required(.String).stringValue } },
            bytes: try json["bytes"].optional(.Array).array.map {try $0.map { try $0.required(.String).stringValue } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let ints = self.ints {
            dict["ints"] = ints
        }
        if let longs = self.longs {
            dict["longs"] = longs
        }
        if let floats = self.floats {
            dict["floats"] = floats
        }
        if let doubles = self.doubles {
            dict["doubles"] = doubles
        }
        if let booleans = self.booleans {
            dict["booleans"] = booleans
        }
        if let strings = self.strings {
            dict["strings"] = strings
        }
        if let bytes = self.bytes {
            dict["bytes"] = bytes
        }
        return dict
    }
}
public func ==(lhs: WithPrimitivesArray, rhs: WithPrimitivesArray) -> Bool {
    return (
        (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
        (lhs.longs == nil ? (rhs.longs == nil) : lhs.longs! == rhs.longs!) &&
        (lhs.floats == nil ? (rhs.floats == nil) : lhs.floats! == rhs.floats!) &&
        (lhs.doubles == nil ? (rhs.doubles == nil) : lhs.doubles! == rhs.doubles!) &&
        (lhs.booleans == nil ? (rhs.booleans == nil) : lhs.booleans! == rhs.booleans!) &&
        (lhs.strings == nil ? (rhs.strings == nil) : lhs.strings! == rhs.strings!) &&
        (lhs.bytes == nil ? (rhs.bytes == nil) : lhs.bytes! == rhs.bytes!) &&
        true
    )
}
