import Foundation
import SwiftyJSON

/**
    A fortune.
*/
public struct Fortune: Serializable {
    
    /**
        The fortune telling.
    */
    public let telling: FortuneTelling?
    
    public let createdAt: String?
    
    public init(
        telling: FortuneTelling?,
        createdAt: String?
    ) {
        self.telling = telling
        self.createdAt = createdAt
    }
    
    public static func readJSON(json: JSON) throws -> Fortune {
        return Fortune(
            telling: try json["telling"].optional(.Dictionary).json.map { try FortuneTelling.readJSON($0) },
            createdAt: try json["createdAt"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let telling = self.telling {
            dict["telling"] = telling.writeData()
        }
        if let createdAt = self.createdAt {
            dict["createdAt"] = createdAt
        }
        return dict
    }
}
