import Foundation
import SwiftyJSON

public struct RecordKey: JSONSerializable {
    
    public let x: Int?
    
    public let y: Bool?
    
    public init(
        x: Int?,
        y: Bool?
    ) {
        self.x = x
        self.y = y
    }
    
    public static func read(json: JSON) -> RecordKey {
        return RecordKey(
            x: json["x"].int,
            y: json["y"].bool
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let x = self.x {
            json["x"] = JSON(x)
        }
        if let y = self.y {
            json["y"] = JSON(y)
        }
        return JSON(json)
    }
}
