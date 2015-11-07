import Foundation
import SwiftyJSON

public struct WithPrimitivesMap: Serializable, Equatable {
    
    public let ints: [String: Int]?
    
    public let longs: [String: Int]?
    
    public let floats: [String: Float]?
    
    public let doubles: [String: Double]?
    
    public let booleans: [String: Bool]?
    
    public let strings: [String: String]?
    
    public let bytes: [String: String]?
    
    public init(
        ints: [String: Int]?,
        longs: [String: Int]?,
        floats: [String: Float]?,
        doubles: [String: Double]?,
        booleans: [String: Bool]?,
        strings: [String: String]?,
        bytes: [String: String]?
    ) {
        self.ints = ints
        self.longs = longs
        self.floats = floats
        self.doubles = doubles
        self.booleans = booleans
        self.strings = strings
        self.bytes = bytes
    }
    
    public static func readJSON(json: JSON) throws -> WithPrimitivesMap {
        return WithPrimitivesMap(
            ints: try json["ints"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Number).intValue } },
            longs: try json["longs"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Number).intValue } },
            floats: try json["floats"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Number).floatValue } },
            doubles: try json["doubles"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Number).doubleValue } },
            booleans: try json["booleans"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Bool).boolValue } },
            strings: try json["strings"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.String).stringValue } },
            bytes: try json["bytes"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.String).stringValue } }
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
public func ==(lhs: WithPrimitivesMap, rhs: WithPrimitivesMap) -> Bool {
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
