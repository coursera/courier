import Foundation
import SwiftyJSON

public struct WithTypedKeyMap: Serializable {
    
    public let ints: [String: String]?
    
    public let longs: [String: String]?
    
    public let floats: [String: String]?
    
    public let doubles: [String: String]?
    
    public let booleans: [String: String]?
    
    public let strings: [String: String]?
    
    public let bytes: [String: String]?
    
    public let record: [String: String]?
    
    public let array: [String: String]?
    
    public let `enum`: [String: String]?
    
    public let custom: [String: String]?
    
    public let fixed: [String: String]?
    
    public init(
        ints: [String: String]?,
        longs: [String: String]?,
        floats: [String: String]?,
        doubles: [String: String]?,
        booleans: [String: String]?,
        strings: [String: String]?,
        bytes: [String: String]?,
        record: [String: String]?,
        array: [String: String]?,
        `enum`: [String: String]?,
        custom: [String: String]?,
        fixed: [String: String]?
    ) {
        self.ints = ints
        self.longs = longs
        self.floats = floats
        self.doubles = doubles
        self.booleans = booleans
        self.strings = strings
        self.bytes = bytes
        self.record = record
        self.array = array
        self.`enum` = `enum`
        self.custom = custom
        self.fixed = fixed
    }
    
    public static func readJSON(json: JSON) throws -> WithTypedKeyMap {
        return WithTypedKeyMap(
            ints: try json["ints"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            longs: try json["longs"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            floats: try json["floats"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            doubles: try json["doubles"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            booleans: try json["booleans"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            strings: try json["strings"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            bytes: try json["bytes"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            record: try json["record"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            array: try json["array"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            `enum`: try json["enum"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            custom: try json["custom"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } },
            fixed: try json["fixed"].optional(.Dictionary).dictionary.map { try $0.mapValues { try $0.required(.String).stringValue } }
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
        if let record = self.record {
            dict["record"] = record
        }
        if let array = self.array {
            dict["array"] = array
        }
        if let `enum` = self.`enum` {
            dict["enum"] = `enum`
        }
        if let custom = self.custom {
            dict["custom"] = custom
        }
        if let fixed = self.fixed {
            dict["fixed"] = fixed
        }
        return dict
    }
}
