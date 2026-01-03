//
//  MediaCollectionViewCell.swift
//  Sporthor
//
//  Created by Mesut on 15.04.2025.
//

import AutoLayout
import ChatKit
import ComponentKit
import ComponentBaseKit
import CryptoKit
import DesignKit
import UIKit
import AVFoundation
import Factory
import ThumbnailProviderKit

protocol MediaCollectionViewCellDelegate: AnyObject {
    func didTapMediaItem(image: UIImage?, videoUrl: URL?, type: ChatMessageType)
}

final class MediaCollectionViewCell: UICollectionViewCell, ReusableView {
    
    // MARK: - Private UI Elements
    
    private lazy var containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap))
        view.addGestureRecognizer(tapGesture)
        view.isUserInteractionEnabled = true
        return view
    }()

    private let imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFill
        imageView.clipsToBounds = true
        return imageView
    }()
    
    private lazy var playButton: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.playCircle.image
        imageView.tintColor = .black
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.isHidden = true
        return imageView
    }()
    
    // MARK: - Private Properties

    private weak var delegate: MediaCollectionViewCellDelegate?
    private var mediaType: ChatMessageType = .image
    private var mediaImage: UIImage?
    private var content: String?
    private var fileType: String?
    @LazyInjected(\.thumbnailProvider) private var mediaThumbnailProvider

    // MARK: - Initialize
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Setup
    
    private func setupUI() {
        contentView.addSubview(containerView) {
            $0.pin(to: contentView)
        }
        containerView.addSubview(imageView) {
            $0.pin(to: containerView)
        }
        contentView.addSubview(playButton) {
            $0.centerX == contentView.centerXAnchor
            $0.centerY == contentView.centerYAnchor
            $0.width == 40
            $0.height == 40
        }
    }
    
    // MARK: - Configure

    func configure(delegate: MediaCollectionViewCellDelegate?, with item: ChatMessageResponse) {
        self.delegate = delegate
        self.mediaType = ChatMessageType(rawValue: item.messageType ?? 1) ?? .image
        if let content = item.content {
            self.content = content
            self.fileType = item.fileExtension
            
            let isVideo = mediaType == .video
            mediaThumbnailProvider.getThumbnail(
                for: content,
                fileType: fileType,
                isVideo: isVideo
            ) { [weak self] thumbnail, isVideo in
                DispatchQueue.main.async {
                    self?.imageView.image = thumbnail
                    self?.playButton.isHidden = !isVideo
                }
            }
        }
    }
    
    @objc
    private func handleTap() {
        if let content = content, let fileUrl = base64ToFile(with: content) {
            switch mediaType {
            case .image:
                if let image = UIImage(contentsOfFile: fileUrl.path) {
                    delegate?.didTapMediaItem(image: image, videoUrl: nil, type: .image)
                }
            case .video:
                delegate?.didTapMediaItem(image: nil, videoUrl: fileUrl, type: .video)
            default:
                break
            }
        }
    }
}
        
private extension MediaCollectionViewCell {
    
    func saveVideoToTemporary(data: Data, attachment: String) -> URL? {
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
    
    func base64ToFile(with base64: String) -> URL? {
        guard let data = Data(base64Encoded: base64) else { return nil }
        return saveVideoToTemporary(data: data, attachment: fileType ?? ".mp4")
    }
}
