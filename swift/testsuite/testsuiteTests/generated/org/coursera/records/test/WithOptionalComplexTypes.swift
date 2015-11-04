import Foundation
import SwiftyJSON

public struct WithOptionalComplexTypes: JSONSerializable {
    
    public let record: Simple?
    
    public let `enum`: Fruits?
    
    public let union: Union?
    
    public let array: [Int]?
    
    public let map: [String: Int]?
    
    public let custom: Int?
    
    public init(
        record: Simple?,
        `enum`: Fruits?,
        union: Union?,
        array: [Int]?,
        map: [String: Int]?,
        custom: Int?
    ) {
        self.record = record
        self.`enum` = `enum`
        self.union = union
        self.array = array
        self.map = map
        self.custom = custom
    }
    
    public enum Union: JSONSerializable {
        case IntMember(Int)
        case StringMember(String)
        case SimpleMember(Simple)
        case UNKNOWN$([String : JSON])
        public static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            if let member = dictionary["int"] {
                return .IntMember(member.intValue)
            }
            if let member = dictionary["string"] {
                return .StringMember(member.stringValue)
            }
            if let member = dictionary["org.coursera.records.test.Simple"] {
                return .SimpleMember(Simple.read(member.jsonValue))
            }
            return .UNKNOWN$(dictionary)
        }
        public func write() -> JSON {
            switch self {
            case .IntMember(let member):
                return JSON(["int": JSON(member)]);
            case .StringMember(let member):
                return JSON(["string": JSON(member)]);
            case .SimpleMember(let member):
                return JSON(["org.coursera.records.test.Simple": member.write()]);
            case .UNKNOWN$(let dictionary):
                return JSON(dictionary)
            }
        }
    }
    
    public static func read(json: JSON) -> WithOptionalComplexTypes {
        return WithOptionalComplexTypes(
            record: json["record"].json.map { Simple.read($0) },
            `enum`: json["enum"].string.map { Fruits.read($0) },
            union: json["union"].json.map { Union.read($0) },
            array: json["array"].array.map { $0.map { $0.intValue } },
            map: json["map"].dictionary.map { $0.mapValues { $0.intValue } },
            custom: json["custom"].int
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let record = self.record {
            json["record"] = record.write()
        }
        if let `enum` = self.`enum` {
            json["enum"] = JSON(`enum`.write())
        }
        if let union = self.union {
            json["union"] = union.write()
        }
        if let array = self.array {
            json["array"] = JSON(array)
        }
        if let map = self.map {
            json["map"] = JSON(map)
        }
        if let custom = self.custom {
            json["custom"] = JSON(custom)
        }
        return JSON(json)
    }
}

