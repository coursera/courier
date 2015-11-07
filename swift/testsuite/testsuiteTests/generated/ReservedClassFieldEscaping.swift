import Foundation
import SwiftyJSON

public struct ReservedClassFieldEscaping: Serializable {
    
    public let json: String?
    
    public let read: String?
    
    public let write: String?
    
    public init(
        json: String?,
        read: String?,
        write: String?
    ) {
        self.json = json
        self.read = read
        self.write = write
    }
    
    public static func readJSON(json: JSON) throws -> ReservedClassFieldEscaping {
        return ReservedClassFieldEscaping(
            json: try json["json"].optional(.String).string,
            read: try json["read"].optional(.String).string,
            write: try json["write"].optional(.String).string
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let json = self.json {
            dict["json"] = json
        }
        if let read = self.read {
            dict["read"] = read
        }
        if let write = self.write {
            dict["write"] = write
        }
        return dict
    }
}
