import Foundation
import SwiftyJSON

public struct WithComplexTypesUnion: JSONSerializable, DataTreeSerializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: JSONSerializable, DataTreeSerializable {
        case EmptyMember(Empty)
        case FruitsMember(Fruits)
        case ArrayMember([Simple])
        case MapMember([String: Simple])
        case FixedMember(String)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            let dict = json.dictionaryValue
            if let member = dict["org.coursera.records.test.Empty"] {
                return .EmptyMember(try Empty.readJSON(member.jsonValue))
            }
            if let member = dict["org.coursera.enums.Fruits"] {
                return .FruitsMember(Fruits.read(member.stringValue))
            }
            if let member = dict["array"] {
                return .ArrayMember(try member.arrayValue.map { try Simple.readJSON($0.jsonValue) })
            }
            if let member = dict["map"] {
                return .MapMember(try member.dictionaryValue.mapValues { try Simple.readJSON($0.jsonValue) })
            }
            if let member = dict["org.coursera.fixed.Fixed8"] {
                return .FixedMember(member.stringValue)
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
            case .EmptyMember(let member):
                return ["org.coursera.records.test.Empty": member.writeData()];
            case .FruitsMember(let member):
                return ["org.coursera.enums.Fruits": member.write()];
            case .ArrayMember(let member):
                return ["array": member.map { $0.writeData() }];
            case .MapMember(let member):
                return ["map": member.mapValues { $0.writeData() }];
            case .FixedMember(let member):
                return ["org.coursera.fixed.Fixed8": member];
            case .UNKNOWN$(let dict):
                return dict
            }
        }
    }
    
    public static func readJSON(json: JSON) throws -> WithComplexTypesUnion {
        return WithComplexTypesUnion(
            union: try json["union"].json.map { try Union.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithComplexTypesUnion {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        return dict
    }
}

