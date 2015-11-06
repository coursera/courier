import Foundation
import SwiftyJSON

public struct WithDateTime: JSONSerializable, DataTreeSerializable {
    
    public let time: Int?
    
    public init(
        time: Int?
    ) {
        self.time = time
    }
    
    public static func readJSON(json: JSON) throws -> WithDateTime {
        return WithDateTime(
            time: json["time"].int
        )
    }
    public func writeJSON() -> JSON {
        return JSON(self.writeData())
    }
    public static func readData(data: [String: AnyObject]) throws -> WithDateTime {
        return try readJSON(JSON(data))
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let time = self.time {
            dict["time"] = time
        }
        return dict
    }
}
