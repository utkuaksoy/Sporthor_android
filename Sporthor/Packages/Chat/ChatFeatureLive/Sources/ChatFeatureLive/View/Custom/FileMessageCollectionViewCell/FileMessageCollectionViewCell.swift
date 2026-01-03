//
//  FileMessageCollectionViewCell.swift
//  ChatFeatureLive
//
//  Created by Mesut Canbaz on 15.02.2025.
//

import DesignKit
import Factory
import UIKit
import QuickLook
import AVKit
import MessageKit
import ChatKit

final class FileMessageCollectionViewCell: MessageContentCell {
    
    // MARK: - Private UI Elements
    
    private let containerView: UIView = {
        let view = UIView()
        view.layer.cornerRadius = 12
        view.layer.masksToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let fileIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.contentMode = .scaleAspectFit
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 40).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        return imageView
    }()
    
    private let fileNameLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 14, weight: .semibold)
        label.numberOfLines = 1
        label.lineBreakMode = .byTruncatingMiddle
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.defaultHigh, for: .vertical)
        label.setContentCompressionResistancePriority(.required, for: .vertical)
        return label
    }()
    
    private let fileSizeLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 12)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.setContentHuggingPriority(.defaultLow, for: .vertical)
        label.setContentCompressionResistancePriority(.defaultHigh, for: .vertical)
        return label
    }()
    
    private let arrowIcon: UIImageView = {
        let imageView = UIImageView()
        imageView.image = UIImage(systemName: "arrow.down.circle.fill")
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 24).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 24).isActive = true
        return imageView
    }()
    
    private lazy var fileInfoStack: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [fileNameLabel, fileSizeLabel])
        stackView.axis = .vertical
        stackView.spacing = 2
        stackView.alignment = .leading
        stackView.distribution = .fillProportionally
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var mainStack: UIStackView = {
        let stackView = UIStackView(arrangedSubviews: [fileIcon, fileInfoStack, arrowIcon])
        stackView.axis = .horizontal
        stackView.spacing = 12
        stackView.alignment = .center
        stackView.distribution = .fill
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var tapGesture: UITapGestureRecognizer = {
        let gesture = UITapGestureRecognizer(target: self, action: #selector(handleTap))
        return gesture
    }()
    
    // MARK: - Private Properties
    
    private var fileURL: URL?
    private var isFromCurrentSender: Bool = false
    private var fileType: String = ""
    private var previewController: QLPreviewController?
    private var videoPlayer: AVPlayerViewController?
    
    @LazyInjected(\.chatCoordinator) private var chatCoordinator
    
    // MARK: - Initialization
    
    deinit {
        print("\(self) deinit ✅")
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    override func configure(
        with message: MessageType,
        at indexPath: IndexPath,
        and messagesCollectionView: MessagesCollectionView
    ) {
        super.configure(
            with: message,
            at: indexPath,
            and: messagesCollectionView
        )
        
        guard case let .custom(data) = message.kind,
              let fileData = data as? FileMessageData else {
            return
        }
        
        self.isFromCurrentSender = messagesCollectionView.messagesDataSource?.isFromCurrentSender(message: message) ?? false
        
        avatarView.isHidden = false
        messageTopLabel.isHidden = isFromCurrentSender
        
        if !isFromCurrentSender {
            messageTopLabel.text = message.sender.displayName
        }
        
        messageBottomLabel.textAlignment = isFromCurrentSender ? .right : .left
        
        fileNameLabel.text = getDisplayName(for: fileData.fileURL)
        fileSizeLabel.text = getFileSize(for: fileData.fileURL)
        fileIcon.image = getFileIcon(for: fileData.fileType)?.withRenderingMode(.alwaysTemplate)
        
        self.fileURL = fileData.fileURL
        self.fileType = fileData.fileType
        
        updateLayout()
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        fileURL = nil
        fileNameLabel.text = nil
        fileSizeLabel.text = nil
        messageTopLabel.text = nil
        messageBottomLabel.text = nil
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        messageContainerView.layoutMargins = .zero
        messageContainerView.directionalLayoutMargins = .zero
    }
    
    // MARK: - Setup UI
    
    private func setupUI() {
        messageContainerView.addSubview(mainStack)
        messageContainerView.layer.cornerRadius = 12
        messageContainerView.layer.masksToBounds = true
        
        messageContainerView.addGestureRecognizer(tapGesture)
        messageContainerView.isUserInteractionEnabled = true
        
        // Stack view configurations
        fileInfoStack.alignment = .leading
        mainStack.distribution = .fill
        
        // Content priorities
        fileInfoStack.setContentHuggingPriority(.defaultLow, for: .horizontal)
        fileIcon.setContentHuggingPriority(.required, for: .horizontal)
        arrowIcon.setContentHuggingPriority(.required, for: .horizontal)
        
        // Main stack constraints
        NSLayoutConstraint.activate([
            mainStack.topAnchor.constraint(equalTo: messageContainerView.topAnchor, constant: 12),
            mainStack.bottomAnchor.constraint(equalTo: messageContainerView.bottomAnchor, constant: -12),
            mainStack.leadingAnchor.constraint(equalTo: messageContainerView.leadingAnchor, constant: 12),
            mainStack.trailingAnchor.constraint(equalTo: messageContainerView.trailingAnchor, constant: -12)
        ])
    }
    
    private func updateLayout() {
        if isFromCurrentSender {
            messageContainerView.backgroundColor = ColorName.backgroundPrimaryGreen.color
            fileNameLabel.textColor = .white
            fileSizeLabel.textColor = .white.withAlphaComponent(0.8)
            fileIcon.tintColor = .white
            arrowIcon.tintColor = .white
            
            NSLayoutConstraint.activate([
                messageContainerView.trailingAnchor.constraint(equalTo: messageContainerView.trailingAnchor, constant: -8),
                messageContainerView.leadingAnchor.constraint(greaterThanOrEqualTo: messageContainerView.leadingAnchor, constant: messageContainerView.frame.width * 0.25)
            ])
        } else {
            messageContainerView.backgroundColor = .white
            fileNameLabel.textColor = .label
            fileSizeLabel.textColor = .secondaryLabel
            fileIcon.tintColor = ColorName.backgroundPrimaryGreen.color
            arrowIcon.tintColor = ColorName.backgroundPrimaryGreen.color
            
            NSLayoutConstraint.activate([
                messageContainerView.leadingAnchor.constraint(equalTo: messageContainerView.leadingAnchor, constant: 8),
                messageContainerView.trailingAnchor.constraint(lessThanOrEqualTo: messageContainerView.trailingAnchor, constant: -messageContainerView.frame.width * 0.25)
            ])
        }
        layoutIfNeeded()
    }
    
    @objc
    private func handleTap() {
        guard let topViewController = UIApplication.shared.keyWindow?.topViewController(),
              let fileURL = fileURL else { return }
        chatCoordinator?.startDocumentPreview(
            presenter: topViewController,
            with: fileURL,
            fileType: fileType
        )
    }
    
    // MARK: - Helper Methods
    
    private func getFileSize(for fileURL: URL) -> String {
        do {
            let resourceValues = try fileURL.resourceValues(forKeys: [.fileSizeKey])
            if let fileSize = resourceValues.fileSize {
                return ByteCountFormatter.string(fromByteCount: Int64(fileSize), countStyle: .file)
            }
        } catch {
            return "Bilinmeyen Boyut"
        }
        return ""
    }
    
    private func getFileIcon(for fileType: String) -> UIImage? {
        switch fileType.lowercased() {
        case "pdf": return UIImage(systemName: "doc.richtext")
        case "png", "jpg", "jpeg": return UIImage(systemName: "photo.fill")
        case "zip": return UIImage(systemName: "archivebox.fill")
        case "mp4", "mov": return UIImage(systemName: "film")
        default: return UIImage(systemName: "doc.fill")
        }
    }
    
    private func getDisplayName(for fileURL: URL) -> String {
        do {
            let resourceValues = try fileURL.resourceValues(forKeys: [.localizedNameKey])
            if let name = resourceValues.localizedName {
                return name
            }
        } catch {
            print("⚠️ Dosya adı alınamadı: \(error)")
        }
        return fileURL.lastPathComponent
    }
    
}


private extension FileMessageCollectionViewCell {
    func isAllowedFileType(url: URL) -> Bool {
        var allowedTypes: [UTType] = [
            .pdf,
            .jpeg,
            .png,
            .plainText,
            .zip,
            .rtf,
            .data,
            .image
        ]
        
        if let docType = UTType(filenameExtension: "doc") {
            allowedTypes.append(docType)
        }
        if let docxType = UTType(filenameExtension: "docx") {
            allowedTypes.append(docxType)
        }
        if let fileUTType = UTType(filenameExtension: fileType) {
            return allowedTypes.contains(fileUTType)
        }
        return false
    }
}

// MARK: - UIApplication Extension
private extension UIWindow {
    func topViewController() -> UIViewController? {
        var top = self.rootViewController
        while true {
            if let presented = top?.presentedViewController {
                top = presented
            } else if let nav = top as? UINavigationController {
                top = nav.visibleViewController
            } else if let tab = top as? UITabBarController {
                top = tab.selectedViewController
            } else {
                break
            }
        }
        return top
    }
}

private extension UIApplication {
    var keyWindow: UIWindow? {
        return UIApplication.shared.connectedScenes
            .filter { $0.activationState == .foregroundActive }
            .first(where: { $0 is UIWindowScene })
            .flatMap({ $0 as? UIWindowScene })?.windows
            .first(where: \.isKeyWindow)
    }
}
