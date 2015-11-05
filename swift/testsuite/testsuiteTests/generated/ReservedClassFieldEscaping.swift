import Foundation
import SwiftyJSON

public struct ReservedClassFieldEscaping: JSONSerializable, DataTreeSerializable {
    
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
    
    public static func readJSON(json: JSON) -> ReservedClassFieldEscaping {
        return ReservedClassFieldEscaping(
            json: json["json"].string,
            read: json["read"].string,
            write: json["write"].string
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) -> ReservedClassFieldEscaping {
        return readJSON(JSON(data))
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
