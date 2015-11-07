import Foundation
import SwiftyJSON

public struct WithComplexTypesUnion: Serializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: Serializable {
        case EmptyMember(Empty)
        case FruitsMember(Fruits)
        case ArrayMember([Simple])
        case MapMember([String: Simple])
        case FixedMember(String)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            let dict = json.dictionaryValue
            if let member = dict["org.coursera.records.test.Empty"] {
                return .EmptyMember(try Empty.readJSON(try member.required(.Dictionary).jsonValue))
            }
            if let member = dict["org.coursera.enums.Fruits"] {
                return .FruitsMember(try Fruits.read(try member.required(.String).stringValue))
            }
            if let member = dict["array"] {
                return .ArrayMember(try member.required(.Array).arrayValue.map { try Simple.readJSON(try $0.required(.Dictionary).jsonValue) })
            }
            if let member = dict["map"] {
                return .MapMember(try member.required(.Dictionary).dictionaryValue.mapValues { try Simple.readJSON(try $0.required(.Dictionary).jsonValue) })
            }
            if let member = dict["org.coursera.fixed.Fixed8"] {
                return .FixedMember(try member.required(.String).stringValue)
            }
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Union must be a JSON object.")
            }
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
            union: try json["union"].optional(.Dictionary).json.map {try Union.readJSON($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let union = self.union {
            dict["union"] = union.writeData()
        }
        return dict
    }
}

