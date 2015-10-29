import Foundation
import SwiftyJSON

struct WithPrimitivesUnion: JSONSerializable {
    
    let union: Union?
    
    init(
        union: Union?
    ) {
        self.union = union
    }
    
    enum Union: JSONSerializable {
        case IntMember(Int)
        case LongMember(Int)
        case FloatMember(Float)
        case DoubleMember(Double)
        case BooleanMember(Bool)
        case StringMember(String)
        case BytesMember(String)
        case UNKNOWN$([String : JSON])
        static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            if let member = dictionary["int"] {
                return .IntMember(member.intValue)
            }
            if let member = dictionary["long"] {
                return .LongMember(member.intValue)
            }
            if let member = dictionary["float"] {
                return .FloatMember(member.floatValue)
            }
            if let member = dictionary["double"] {
                return .DoubleMember(member.doubleValue)
            }
            if let member = dictionary["boolean"] {
                return .BooleanMember(member.boolValue)
            }
            if let member = dictionary["string"] {
                return .StringMember(member.stringValue)
            }
            if let member = dictionary["bytes"] {
                return .BytesMember(member.stringValue)
            }
            return .UNKNOWN$(dictionary)
        }
        func write() -> JSON {
            switch self {
            case .IntMember(let member):
                return JSON(["int": JSON(member)]);
            case .LongMember(let member):
                return JSON(["long": JSON(member)]);
            case .FloatMember(let member):
                return JSON(["float": JSON(member)]);
            case .DoubleMember(let member):
                return JSON(["double": JSON(member)]);
            case .BooleanMember(let member):
                return JSON(["boolean": JSON(member)]);
            case .StringMember(let member):
                return JSON(["string": JSON(member)]);
            case .BytesMember(let member):
                return JSON(["bytes": JSON(member)]);
            case .UNKNOWN$(let dictionary):
                return JSON(dictionary)
            }
        }
    }
    
    static func read(json: JSON) -> WithPrimitivesUnion {
        return WithPrimitivesUnion(
            union: json["union"].json.map { Union.read($0) }
        )
    }
    func write() -> JSON {
        var json: [String : JSON] = [:]
        if let union = self.union {
            json["union"] = union.write()
        }
        return JSON(json)
    }
}

