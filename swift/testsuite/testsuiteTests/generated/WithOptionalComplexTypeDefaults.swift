import Foundation
import SwiftyJSON

public struct WithOptionalComplexTypeDefaults: JSONSerializable, DataTreeSerializable {
    
    public let record: Simple?
    
    public let `enum`: Fruits?
    
    public let union: Union?
    
    public let array: [Int]?
    
    public let map: [String: Int]?
    
    public let custom: Int?
    
    public init(
        record: Simple? = Simple(message: "defaults!"),
        `enum`: Fruits? = .APPLE,
        union: Union? = .IntMember(1),
        array: [Int]? = [1],
        map: [String: Int]? = ["a": 1],
        custom: Int? = 1
    ) {
        self.record = record
        self.`enum` = `enum`
        self.union = union
        self.array = array
        self.map = map
        self.custom = custom
    }
    
    public enum Union: JSONSerializable, DataTreeSerializable {
        case IntMember(Int)
        case StringMember(String)
        case SimpleMember(Simple)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) -> Union {
            let dict = json.dictionaryValue
            if let member = dict["int"] {
                return .IntMember(member.intValue)
            }
            if let member = dict["string"] {
                return .StringMember(member.stringValue)
            }
            if let member = dict["org.coursera.records.test.Simple"] {
                return .SimpleMember(Simple.readJSON(member.jsonValue))
            }
            return .UNKNOWN$(json.dictionaryObject!)
        }
        public func writeJSON() -> JSON {
            return JSON(self.writeData())
        }
        public static func readData(data: [String: AnyObject]) -> Union {
            return readJSON(JSON(data))
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
    
    public static func readJSON(json: JSON) -> WithOptionalComplexTypeDefaults {
        return WithOptionalComplexTypeDefaults(
            record: json["record"].json.map { Simple.readJSON($0) },
            `enum`: json["enum"].string.map { Fruits.read($0) },
            union: json["union"].json.map { Union.readJSON($0) },
            array: json["array"].array.map { $0.map { $0.intValue } },
            map: json["map"].dictionary.map { $0.mapValues { $0.intValue } },
            custom: json["custom"].int
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithOptionalComplexTypeDefaults {
        return readJSON(JSON(data))
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
        if let custom = self.custom {
            dict["custom"] = custom
        }
        return dict
    }
}

