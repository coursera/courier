import Foundation
import SwiftyJSON

public struct WithRecordCustomTypeUnion: Serializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: Serializable {
        case MessageMember(Message)
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            let dict = json.dictionaryValue
            if let member = dict["org.coursera.records.test.Message"] {
                return .MessageMember(try Message.readJSON(try member.required(.Dictionary).jsonValue))
            }
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Union must be a JSON object.")
            }
        }
        public func writeData() -> [String: AnyObject] {
            switch self {
            case .MessageMember(let member):
                return ["org.coursera.records.test.Message": member.writeData()];
            case .UNKNOWN$(let dict):
                return dict
            }
        }
    }
    
    public static func readJSON(json: JSON) throws -> WithRecordCustomTypeUnion {
        return WithRecordCustomTypeUnion(
            union: try json["union"].optional(.Dictionary).json.map { try Union.readJSON($0) }
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

