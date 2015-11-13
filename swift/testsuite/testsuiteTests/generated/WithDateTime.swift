import Foundation
import SwiftyJSON

public struct WithDateTime: Serializable {
    
    public let time: NSDate?
    
    public init(
        time: NSDate?
    ) {
        self.time = time
    }
    
    public static func readJSON(json: JSON) throws -> WithDateTime {
        return WithDateTime(
            time: try json["time"].optional(.Number).int.map { try NSDateCoercer.coerceInput($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let time = self.time {
            dict["time"] = NSDateCoercer.coerceOutput(time)
        }
        return dict
    }
}
