import Foundation
import SwiftyJSON

public struct WithComplexTypes: JSONSerializable, DataTreeSerializable, Equatable {
    
    public let record: Simple?
    
    public let `enum`: Fruits?
    
    public let union: Union?
    
    public let array: [Int]?
    
    public let map: [String: Int]?
    
    public let complexMap: [String: Simple]?
    
    public let custom: Int?
    
    public init(
        record: Simple?,
        `enum`: Fruits?,
        union: Union?,
        array: [Int]?,
        map: [String: Int]?,
        complexMap: [String: Simple]?,
        custom: Int?
    ) {
        self.record = record
        self.`enum` = `enum`
        self.union = union
        self.array = array
        self.map = map
        self.complexMap = complexMap
        self.custom = custom
    }
    
    public enum Union: JSONSerializable, DataTreeSerializable, Equatable {
        case IntMember(Int)
        case StringMember(String)
        case SimpleMember(Simple)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            let dict = json.dictionaryValue
            if let member = dict["int"] {
                return .IntMember(member.intValue)
            }
            if let member = dict["string"] {
                return .StringMember(member.stringValue)
            }
            if let member = dict["org.coursera.records.test.Simple"] {
                return .SimpleMember(try Simple.readJSON(member.jsonValue))
            }
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError.MalformedUnion
            }
        }
        public func writeJSON() -> JSON {
            return JSON(self.writeData())
        }
        public static func readData(data: [String: AnyObject]) throws -> Union {
            return try readJSON(JSON(data))
        }
        public func writeData() -> [String: AnyObject] {
            switch self {
            case .IntMember(let member):
                return ["int": member];
            case .StringMember(let member):
                return ["string": member];
            case .SimpleMember(let member):
                return ["org.coursera.records.test.Simple": member.writeData()];
            case .UNKNOWN$(let dict):
                return dict
            }
        }
    }
    
    public static func readJSON(json: JSON) throws -> WithComplexTypes {
        return WithComplexTypes(
            record: try json["record"].json.map { try Simple.readJSON($0) },
            `enum`: json["enum"].string.map { Fruits.read($0) },
            union: try json["union"].json.map { try Union.readJSON($0) },
            array: json["array"].array.map { $0.map { $0.intValue } },
            map: json["map"].dictionary.map { $0.mapValues { $0.intValue } },
            complexMap: try json["complexMap"].dictionary.map { try $0.mapValues { try Simple.readJSON($0.jsonValue) } },
            custom: json["custom"].int
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithComplexTypes {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let record = self.record {
            dict["record"] = record.writeData()
        }
        if let `enum` = self.`enum` {
            dict["enum"] = `enum`.write()
        }
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        if let array = self.array {
            dict["array"] = array
        }
        if let map = self.map {
            dict["map"] = map
        }
        if let complexMap = self.complexMap {
            dict["complexMap"] = complexMap.mapValues { $0.writeData() }
        }
        if let custom = self.custom {
            dict["custom"] = custom
        }
        return dict
    }
}
public func ==(lhs: WithComplexTypes, rhs: WithComplexTypes) -> Bool {
    return (
        (lhs.record == nil ? (rhs.record == nil) : lhs.record! == rhs.record!) &&
        (lhs.`enum` == nil ? (rhs.`enum` == nil) : lhs.`enum`! == rhs.`enum`!) &&
        (lhs.union == nil ? (rhs.union == nil) : lhs.union! == rhs.union!) &&
        (lhs.array == nil ? (rhs.array == nil) : lhs.array! == rhs.array!) &&
        (lhs.map == nil ? (rhs.map == nil) : lhs.map! == rhs.map!) &&
        (lhs.complexMap == nil ? (rhs.complexMap == nil) : lhs.complexMap! == rhs.complexMap!) &&
        (lhs.custom == nil ? (rhs.custom == nil) : lhs.custom! == rhs.custom!) &&
        true
    )
}

public func ==(lhs: WithComplexTypes.Union, rhs: WithComplexTypes.Union) -> Bool {
    switch (lhs, rhs) {
    case (let .IntMember(lhs), let .IntMember(rhs)):
        return lhs == rhs
    case (let .StringMember(lhs), let .StringMember(rhs)):
        return lhs == rhs
    case (let .SimpleMember(lhs), let .SimpleMember(rhs)):
        return lhs == rhs
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return JSON(lhs) == JSON(rhs)
    default:
        return false
    }
}
