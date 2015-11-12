import Foundation
import SwiftyJSON

public struct WithEmptyUnion: Serializable {
    
    public let union: Union?
    
    public init(
        union: Union?
    ) {
        self.union = union
    }
    
    public enum Union: Serializable {
        case UNKNOWN$([String : AnyObject])
        public static func readJSON(json: JSON) throws -> Union {
            if let unknownDict = json.dictionaryObject {
                return .UNKNOWN$(unknownDict)
            } else {
                throw ReadError(cause: "Union must be a JSON object.")
            }
        }
        public func writeData() -> [String: AnyObject] {
            switch self {
            case .UNKNOWN$(let dict):
                return dict
            }
        }
    }
    
    public static func readJSON(json: JSON) throws -> WithEmptyUnion {
        return WithEmptyUnion(
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

