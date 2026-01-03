//
//  CustomAttachmentManager.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 22.03.2025.
//

import InputBarAccessoryView
import UIKit

final class CustomAttachmentManager: AttachmentManager {
    public enum CustomAttachmentType {
        case camera
        case gallery
        case file
    }

    struct CustomAttachment {
        let type: CustomAttachmentType
        let icon: UIImage
        let title: String
    }

    private var customOptions: [CustomAttachment] = []
    
    var customOptionCount: Int {
        customOptions.count
    }
    
    override var showAddAttachmentCell: Bool {
           get { return super.showAddAttachmentCell }
           set { super.showAddAttachmentCell = newValue }
       }
    
    override func invalidate() {
        print("⚠️ CustomAttachmentManager.invalidate() çağrıldı ama override edildi")
        reloadData()
    }

    func setCustomAttachments(_ options: [CustomAttachment]) {
        customOptions.removeAll()
        for (index, option) in options.enumerated() {
            customOptions.append(option)
            insertAttachment(.data(Data()), at: index)
        }
    }

    func customAttachment(at index: Int) -> CustomAttachment? {
        guard index < customOptions.count else { return nil }
        return customOptions[index]
    }
}
