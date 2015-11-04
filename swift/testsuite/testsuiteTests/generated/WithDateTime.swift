import Foundation
import SwiftyJSON

public struct WithDateTime: JSONSerializable {
    
    public let time: Int?
    
    public init(
        time: Int?
    ) {
        self.time = time
    }
    
    public static func read(json: JSON) -> WithDateTime {
        return WithDateTime(
            time: json["time"].int
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let time = self.time {
            json["time"] = JSON(time)
        }
        return JSON(json)
    }
}
