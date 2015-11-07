import Foundation
import SwiftyJSON

public struct WithOptionalComplexTypes: Serializable {
    
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
    
    public enum Union: Serializable {
        case IntMember(Int)
        case StringMember(String)
        case SimpleMember(Simple)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            let dict = json.dictionaryValue
            if let member = dict["int"] {
                return .IntMember(try member.required(.Number).intValue)
            }
            if let member = dict["string"] {
                return .StringMember(try member.required(.String).stringValue)
            }
            if let member = dict["org.coursera.records.test.Simple"] {
                return .SimpleMember(try Simple.readJSON(member.required(.Dictionary).jsonValue))
            }
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Union must be a JSON object.")
            }
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
    
    public static func readJSON(json: JSON) throws -> WithOptionalComplexTypes {
        return WithOptionalComplexTypes(
            record: try json["record"].optional(.Dictionary).json.map {try Simple.readJSON($0) },
            `enum`: try json["enum"].optional(.String).string.map {Fruits.read($0) },
            union: try json["union"].optional(.Dictionary).json.map {try Union.readJSON($0) },
            array: try json["array"].optional(.Array).array.map {try $0.map { try $0.required(.Number).intValue } },
            map: try json["map"].optional(.Dictionary).dictionary.map {try $0.mapValues { try $0.required(.Number).intValue } },
            custom: try json["custom"].optional(.Number).int
        )
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

