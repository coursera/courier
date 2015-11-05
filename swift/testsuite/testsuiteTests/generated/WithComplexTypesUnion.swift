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
        public static func readJSON(json: JSON) -> Union {
            let dict = json.dictionaryValue
            if let member = dict["org.coursera.records.test.Empty"] {
                return .EmptyMember(Empty.readJSON(member.jsonValue))
            }
            if let member = dict["org.coursera.enums.Fruits"] {
                return .FruitsMember(Fruits.read(member.stringValue))
            }
            if let member = dict["array"] {
                return .ArrayMember(member.arrayValue.map { Simple.readJSON($0.jsonValue) })
            }
            if let member = dict["map"] {
                return .MapMember(member.dictionaryValue.mapValues { Simple.readJSON($0.jsonValue) })
            }
            if let member = dict["org.coursera.fixed.Fixed8"] {
                return .FixedMember(member.stringValue)
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
    
    public static func readJSON(json: JSON) -> WithComplexTypesUnion {
        return WithComplexTypesUnion(
            union: json["union"].json.map { Union.readJSON($0) }
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> WithComplexTypesUnion {
        return readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        return dict
    }
}

