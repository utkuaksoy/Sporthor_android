//
//  ChatMessage.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 28.01.2025.
//

import CryptoKit
import Foundation
import MessageKit
import UIKit

public enum ChatMessageType {
    case text
    case image
    case video
    case file
    
    public var socketMessageType: Int {
        switch self {
        case .text:
            return 0
        case .image:
            return 1
        case .video:
            return 2
        case .file:
            return 3
        }
    }
    
    public init?(rawValue: Int) {
          switch rawValue {
          case 0:
              self = .text
          case 1:
              self = .image
          case 2:
              self = .video
          case 3:
              self = .file
          default:
              return nil
          }
      }
}

public struct ChatMessage: MessageKit.MessageType {
    public let messageId: String
    public let sender: SenderType
    public let sentDate: Date
    public let content: String
    public let messageType: ChatMessageType
    public let attachment: String?
    
    public var kind: MessageKind {
        if sender.senderId == "typing" {
               return .custom("typing")
           }
        switch messageType {
        case .text:
            return .text(content)
        case .image:
            if let data = Data(base64Encoded: content),
               let image = UIImage(data: data) {
                let mediaItem = ImageMediaItem(image: image)
                return .photo(mediaItem)
            }
            return .text("🖼 Resim yüklenemedi")
        case .video:
            if let data = Data(base64Encoded: content),
               let url = saveVideoToTemporary(data: data, attachment: attachment ?? ".mp4") {
                let mediaItem = VideoMediaItem(url: url)
                return .video(mediaItem)
            }
            return .text("🎥 Video yüklenemedi")
        case .file:
            if let url = base64ToFile(with: content, fileExtension: attachment ?? "bin") {
                return .custom(FileMessageData(fileURL: url, fileType: attachment ?? ""))
            }
            return .text("📄 Dosya yüklenemedi")
        }
    }
    
    public init(
        messageId: String,
        sender: ChatUser,
        sentDate: Date,
        content: String,
        messageType: ChatMessageType = .text,
        attachment: String? = ""
    ) {
        self.messageId = messageId
        self.sender = sender
        self.sentDate = sentDate
        self.content = content
        self.messageType = messageType
        self.attachment = attachment
    }
    
    private func saveVideoToTemporary(data: Data, attachment: String) -> URL? {
        let hash = SHA256.hash(data: data).compactMap { String(format: "%02x", $0) }.joined()
        let cleanExtension = attachment.trimmingCharacters(in: .whitespaces)
            .replacingOccurrences(of: ".", with: "")
        let fileExtension = "." + cleanExtension
        let fileName = "\(hash)\(fileExtension)"
        
        let cachesDirectoryURL = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first!
        let videoURL = cachesDirectoryURL.appendingPathComponent(fileName)
        
        if FileManager.default.fileExists(atPath: videoURL.path) {
            return videoURL
        }
        
        do {
            try data.write(to: videoURL)
            return videoURL
        } catch {
            return nil
        }
    }
    
    private func base64ToFile(with content: String, fileExtension: String = "bin") -> URL? {
        guard let data = Data(base64Encoded: content) else {
            return nil
        }

        let hash = SHA256.hash(data: data).compactMap { String(format: "%02x", $0) }.joined()
        let fileName = "\(hash).\(fileExtension)"
        
        let tempDirectoryURL = FileManager.default.temporaryDirectory
        let fileURL = tempDirectoryURL.appendingPathComponent(fileName)
        
        if FileManager.default.fileExists(atPath: fileURL.path) {
            return fileURL
        }
        
        do {
            try data.write(to: fileURL)
            print("✅ Dosya başarıyla kaydedildi: \(fileURL.path)")
            return fileURL
        } catch {
            print("❌ Dosya kaydedilemedi: \(error.localizedDescription)")
            return nil
        }
    }
}

public struct FileMessageData {
    public let fileURL: URL
    public let fileType: String
    
    public init(fileURL: URL, fileType: String) {
        self.fileURL = fileURL
        self.fileType = fileType
    }
}

public class ImageMediaItem: MediaItem {
    public var url: URL?
    public var image: UIImage?
    public var placeholderImage: UIImage
    public var size: CGSize
    
    public init(image: UIImage) {
        self.image = image
        self.size = image.size
        self.placeholderImage = UIImage()
    }
}

public class VideoMediaItem: MediaItem {
    public var url: URL?
    public var image: UIImage?
    public var placeholderImage: UIImage
    public var size: CGSize
    
    public init(url: URL) {
        self.url = url
        self.size = CGSize(width: 240, height: 240)
        self.placeholderImage = UIImage(systemName: "video.fill") ?? UIImage()
    }
}

public extension ChatMessage {
    static var typingPlaceholder: ChatMessage {
        return ChatMessage(
            messageId: UUID().uuidString,
            sender: ChatUser(senderId: "typing", displayName: "Yazıyor...", image: ""),
            sentDate: Date(),
            content: "",
            messageType: .text
        )
    }
}
