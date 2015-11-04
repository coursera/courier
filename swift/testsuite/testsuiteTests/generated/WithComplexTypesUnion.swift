import Foundation
import SwiftyJSON

public struct WithComplexTypesUnion: JSONSerializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: JSONSerializable {
        case EmptyMember(Empty)
        case FruitsMember(Fruits)
        case ArrayMember([Simple])
        case MapMember([String: Simple])
        case FixedMember(String)
        case UNKNOWN$([String : JSON])
        public static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            if let member = dictionary["org.coursera.records.test.Empty"] {
                return .EmptyMember(Empty.read(member.jsonValue))
            }
            if let member = dictionary["org.coursera.enums.Fruits"] {
                return .FruitsMember(Fruits.read(member.stringValue))
            }
            if let member = dictionary["array"] {
                return .ArrayMember(member.arrayValue.map { Simple.read($0.jsonValue) })
            }
            if let member = dictionary["map"] {
                return .MapMember(member.dictionaryValue.mapValues { Simple.read($0.jsonValue) })
            }
            if let member = dictionary["org.coursera.fixed.Fixed8"] {
                return .FixedMember(member.stringValue)
            }
            return .UNKNOWN$(dictionary)
        }
        public func write() -> JSON {
            switch self {
            case .EmptyMember(let member):
                return JSON(["org.coursera.records.test.Empty": member.write()]);
            case .FruitsMember(let member):
                return JSON(["org.coursera.enums.Fruits": JSON(member.write())]);
            case .ArrayMember(let member):
                return JSON(["array": JSON(member.map { $0.write() })]);
            case .MapMember(let member):
                return JSON(["map": JSON(member.mapValues { $0.write() })]);
            case .FixedMember(let member):
                return JSON(["org.coursera.fixed.Fixed8": JSON(member)]);
            case .UNKNOWN$(let dictionary):
                return JSON(dictionary)
            }
        }
    }
    
    public static func read(json: JSON) -> WithComplexTypesUnion {
        return WithComplexTypesUnion(
            union: json["union"].json.map { Union.read($0) }
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let union = self.union {
            json["union"] = union.write()
        }
        return JSON(json)
    }
}

