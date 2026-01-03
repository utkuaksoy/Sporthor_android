//
//  ChatViewController.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 28.01.2025.
//

import AVKit
import BarVisibilityKit
import ChatKit
import ComponentKit
import DesignKit
import Factory
import IQKeyboardManagerSwift
import InputBarAccessoryView
import MessageKit
import SignalRServiceKit
import UIKit
import QuickLook
import UniformTypeIdentifiers

final class ChatViewController: MessagesViewController, ChatViewControllerProtocol, NavigationBarVisibility, TabBarVisibility {
    
    // MARK: Private UI Elements
    
    private lazy var chatTitleView: ChatNavigationTitleView = {
        let view = ChatNavigationTitleView()
        return view
    }()
    
    private let chatInputBar: ChatInputBar = {
        let view = ChatInputBar()
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var scrollToBottomButton: UIButton = {
        let button = UIButton(frame: .zero)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.backgroundColor = ColorName.backgroundPrimaryGreen.color
        button.layer.cornerRadius = 20
        button.setImage(UIImage(systemName: "arrow.down"), for: .normal)
        button.tintColor = .white
        button.isHidden = true
        button.addTarget(self, action: #selector(scrollToBottomTapped), for: .touchUpInside)
        return button
    }()
    
    private lazy var refreshControl: UIRefreshControl = {
        let control = UIRefreshControl()
        return control
    }()
    
    private let loadingView = UIActivityIndicatorView(style: .large)
    
    // MARK: - Private Properties

    private var typingIndicatorWorkItem: DispatchWorkItem?
    private var viewModel: ChatViewModelProtocol
    private var previewURL: URL?
    private var previewController: QLPreviewController?
    private var currentPreviewItemURL: URL?
    private weak var chatCoordinatorDelegate: ChatCoordinatorDelegate?
    
    var senderId: String {
        viewModel.chatPartner.senderId
    }
    
    override var canBecomeFirstResponder: Bool {
        return false
    }
    
    private var isAttachmentManagerActive: Bool {
        return chatInputBar.inputPlugins.contains(where: { $0 is AttachmentManager })
    }

    override var inputAccessoryView: UIView? {
        return nil
    }
    
    // MARK: - Initialization
    
    deinit {
        print("\(self) deinit ✅")
    }
    
    init(viewModel: ChatViewModelProtocol, delegate: ChatCoordinatorDelegate?) {
        self.viewModel = viewModel
        self.chatCoordinatorDelegate = delegate
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        self.messagesCollectionView = ChatMessagesCollectionView()
        (self.messagesCollectionView as? ChatMessagesCollectionView)?.messagesCollectionViewDelegate = self
        super.viewDidLoad()
        configureIQKeyboardManager(isEnabled: false)
        setupUI()
        viewModel.viewDidLoad()
        setupDelegates()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureIQKeyboardManager(isEnabled: false)
        viewModel.viewWillAppear()
        configureTabBarVisibility(at: .willAppear(isHidden: true))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: true))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if isMovingFromParent {
            viewModel.viewWillDisappear()
            configureTabBarVisibility(at: .willDisappear)
            configureNavigationBarVisibility(at: .willDisappear)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if isMovingFromParent {
            configureIQKeyboardManager(isEnabled: true)
            configureTabBarVisibility(at: .didDisappear)
        }
    }
    
    // MARK: - Setup
    
    private func setupUI() {
        view.backgroundColor = .white
        setupNavigationBar()
        setupConstraints()
        setupMessageCollectionView()
        setupDelegates()
        setupInputBar()
        setupScrollToBottomButton()
    }
    
    private func setupConstraints() {
        loadingView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(loadingView)
        
        NSLayoutConstraint.activate([
            loadingView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            loadingView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            loadingView.widthAnchor.constraint(equalToConstant: 50),
            loadingView.heightAnchor.constraint(equalToConstant: 50)
        ])
        
        loadingView.hidesWhenStopped = true
    }
    
    private func setupNavigationBar() {
        configureNavigationAppearance()
        chatTitleView.configure(
            username: viewModel.chatPartner.displayName,
            profileImage: viewModel.chatPartner.image
        )
        chatTitleView.delegate = self
        
        let backButton = UIBarButtonItem(
            image: .chevronLeftIcon, style: .done,
            target: self,
            action: #selector(didTapBackButton)
        )
        backButton.tintColor = ColorName.contentStrong900.color
        let containerView = UIView()
        containerView.addSubview(chatTitleView)
        chatTitleView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            chatTitleView.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            chatTitleView.topAnchor.constraint(equalTo: containerView.topAnchor),
            chatTitleView.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
            chatTitleView.trailingAnchor.constraint(lessThanOrEqualTo: containerView.trailingAnchor)
        ])
        navigationItem.leftBarButtonItems = [
            backButton,
            UIBarButtonItem(
                customView: containerView
            )
        ]
    }
    
    private func configureNavigationAppearance() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white
        appearance.shadowColor = nil
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
    }
    
    private func setupScrollToBottomButton() {
        view.addSubview(scrollToBottomButton)
        
        NSLayoutConstraint.activate([
            scrollToBottomButton.widthAnchor.constraint(equalToConstant: 40),
            scrollToBottomButton.heightAnchor.constraint(equalToConstant: 40),
            scrollToBottomButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            scrollToBottomButton.bottomAnchor.constraint(equalTo: chatInputBar.topAnchor, constant: -16)
        ])
    }
    
    private func setupMessageCollectionView() {
        messagesCollectionView.register(
            FileMessageCollectionViewCell.self,
            forCellWithReuseIdentifier: String(describing: FileMessageCollectionViewCell.self)
        )
        
        if let layout = messagesCollectionView.collectionViewLayout as? MessagesCollectionViewFlowLayout {
            layout.textMessageSizeCalculator.messageLabelFont = UIFont.body04Compact
            layout.minimumInteritemSpacing = 2
            layout.minimumLineSpacing = 2
            layout.sectionInset = .init(top: 8, left: 8, bottom: 8, right: 8)
//            layout.sectionFootersPinToVisibleBounds = true
        }
        messagesCollectionView.refreshControl = refreshControl
        messagesCollectionView.scrollIndicatorInsets = messagesCollectionView.contentInset
        messagesCollectionView.backgroundColor = ColorName.backgroundWeak100.color
        messagesCollectionView.keyboardDismissMode = .interactive
        
        messagesCollectionView.isPrefetchingEnabled = true
        scrollsToLastItemOnKeyboardBeginsEditing = true
        maintainPositionOnInputBarHeightChanged = true
        showMessageTimestampOnSwipeLeft = false
    }
    
    private func setupDelegates() {
        viewModel.delegate = self
        messagesCollectionView.messagesDataSource = self
        messagesCollectionView.messagesLayoutDelegate = self
        messagesCollectionView.messagesDisplayDelegate = self
        messagesCollectionView.messageCellDelegate = self
        messagesCollectionView.prefetchDataSource = self
    }
    
    private func setupInputBar() {
        messageInputBar.isHidden = true
        inputContainerView.addSubview(chatInputBar)
        chatInputBar.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            chatInputBar.leadingAnchor.constraint(equalTo: inputContainerView.leadingAnchor),
            chatInputBar.trailingAnchor.constraint(equalTo: inputContainerView.trailingAnchor),
            chatInputBar.topAnchor.constraint(equalTo: inputContainerView.topAnchor),
            chatInputBar.bottomAnchor.constraint(equalTo: inputContainerView.bottomAnchor)
        ])
        chatInputBar.chatDelegate = self
    }
    
    private func configureIQKeyboardManager(isEnabled: Bool) {
        IQKeyboardManager.shared.enable = isEnabled
        IQKeyboardManager.shared.enableAutoToolbar = isEnabled
    }
    
    // MARK: - Actions
    
    @objc
    private func didTapBackButton() {
        if viewModel.isNewCreated {
            chatCoordinatorDelegate?.navigateToBack()
        } else {
            navigationController?.popViewController(animated: true)
        }
    }
    
    @objc
    private func scrollToBottomTapped() {
        guard viewModel.messageCount > 0 else { return }
        messagesCollectionView.scrollToLastItem(animated: true)
    }
    
    private func playVideo(from url: URL) {
        let player = AVPlayer(url: url)
        let playerViewController = AVPlayerViewController()
        playerViewController.player = player

        present(playerViewController, animated: true) {
            player.play()
        }
    }
    
    private func presentImageFullscreen(image: UIImage) {
        Container.shared.chatCoordinator()?.startFullScreenImage(
            presenter: self,
            image: image
        )
    }

    @objc
    private func dismissFullscreenImage() {
        dismiss(animated: true)
    }
}

extension ChatViewController: MessagesCollectionViewDelegate {
    func didTap() {
        self.chatInputBar.dimissKeyboard()
    }
}

// MARK: - ChatViewModelDelegate

extension ChatViewController: ChatViewModelDelegate {
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            
            self.messagesCollectionView.reloadData()
            self.messagesCollectionView.layoutIfNeeded()
            
            if self.viewModel.messageCount > 0 {
                let lastSection = self.viewModel.messageCount - 1
                let indexPath = IndexPath(item: 0, section: lastSection)
                self.messagesCollectionView.scrollToItem(
                    at: indexPath,
                    at: .bottom,
                    animated: false
                )
            }
        }
    }
    
    func showLoading(_ show: Bool) {
        DispatchQueue.main.async {
            if show {
                self.loadingView.startAnimating()
            } else {
                self.loadingView.stopAnimating()
            }
        }
    }
    
    func didReceiveMessage() {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            let newSection = self.viewModel.messageCount - 1
            guard newSection >= 0 else { return }
            
            let currentSections = self.messagesCollectionView.numberOfSections
            
            if newSection >= currentSections {
                self.messagesCollectionView.performBatchUpdates({
                    self.messagesCollectionView.insertSections([newSection])
                }, completion: { [weak self] _ in
                    guard let self = self else { return }
                    if self.viewModel.messageCount > 0 {
                        self.messagesCollectionView.scrollToLastItem(animated: true)
                    }
                })
            } else {
                self.messagesCollectionView.performBatchUpdates({
                    self.messagesCollectionView.reloadSections([newSection])
                }, completion: { [weak self] _ in
                    guard let self = self else { return }
                    if self.viewModel.messageCount > 0 {
                        self.messagesCollectionView.scrollToLastItem(animated: true)
                    }
                })
            }
        }
    }
    
    func didUpdateTypingStatus(isTyping: Bool, username: String) {
//        typingIndicatorWorkItem?.cancel()
//        DispatchQueue.main.async { [weak self] in
//            guard let self = self else { return }
//            self.isTyping = isTyping
//            self.typingSender = viewModel.chatPartner
//            
//            if isTyping {
//                let workItem = DispatchWorkItem { [weak self] in
//                    self?.isTyping = false
//                    self?.typingSender = nil
//                }
//                self.typingIndicatorWorkItem = workItem
//                DispatchQueue.main.asyncAfter(deadline: .now() + 2, execute: workItem)
//            }
//        }
    }
    
    func didUpdateConnectionStatus(isConnected: Bool) {
        // TODO: - Mesut Chat ile connect kurulamadı Ne yapıcaz ?
        DispatchQueue.main.async { [weak self] in
//            self?.showLoading(false)
        }
    }
    
    func didEncounterError(_ error: Error) {
        showLoading(false)
        showError(message: error.localizedDescription)
    }
}

// MARK: - MessagesDataSource

extension ChatViewController: MessagesDataSource {
    
    var currentSender: SenderType {
        return viewModel.currentUser
    }
    
    func messageForItem(
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> MessageType {
        return viewModel.message(at: indexPath.section)
    }
    
    func numberOfSections(in messagesCollectionView: MessagesCollectionView) -> Int {
        return viewModel.messageCount
    }
    
    /// Display Name
    public func messageTopLabelAttributedText(
        for message: MessageType,
        at indexPath: IndexPath
    ) -> NSAttributedString? {
        if !isFromCurrentSender(message: message) {
            return NSAttributedString(
                string: message.sender.displayName,
                attributes: [
                    .font: UIFont.interTight500,
                    .foregroundColor: ColorName.contentSoft600.color
                ]
            )
        }
        return nil
    }
    
    /// Section Date
    public func cellTopLabelAttributedText(
        for message: MessageType,
        at indexPath: IndexPath
    ) -> NSAttributedString? {
        return NSAttributedString(
            string: message.sentDate.toString(format: .date),
            attributes: [
                .font: UIFont.interTight500,
                .foregroundColor: ColorName.contentSoft600.color
            ]
        )
    }
    
    public func messageBottomLabelAttributedText(
        for message: MessageType,
        at indexPath: IndexPath
    ) -> NSAttributedString? {
        return NSAttributedString(
            string: message.sentDate.toString(format: .time),
            attributes: [
                .font: UIFont.interTight500,
                .foregroundColor: ColorName.contentSoft600.color
            ]
        )
    }
    
//    func messageFooterView(for indexPath: IndexPath, in messagesCollectionView: MessagesCollectionView) -> MessageReusableView {
//         let footer = messagesCollectionView.dequeueReusableFooterView(TypingIndicatorFooterView.self, for: indexPath)
//         if isTyping && indexPath.section == viewModel.messageCount {
//             footer.configure(with: typingSender?.displayName ?? "")
//         } else {
//             footer.hide()
//         }
//         return footer
//     }
    
    func customCell(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> UICollectionViewCell {
        guard case .custom = message.kind else {
            fatalError("Did not return a valid cell for MessageKind.custom(Any).")
        }
        
        let cell = messagesCollectionView.dequeueReusableCell(
            FileMessageCollectionViewCell.self,
            for: indexPath
        )
        cell.configure(with: message, at: indexPath, and: messagesCollectionView)
        return cell
    }
}

// MARK: - MessagesLayoutDelegate

extension ChatViewController: MessagesLayoutDelegate {
//    public func footerViewSize(
//        for section: Int,
//        in messagesCollectionView: MessagesCollectionView
//    ) -> CGSize {
////        return isTyping ? CGSize(width: messagesCollectionView.bounds.width, height: 30) : .zero
//    }
    
    public func messageTopLabelHeight(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> CGFloat {
        return !isFromCurrentSender(message: message) ? 20 : 0
    }
    
    public func messageBottomLabelHeight(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> CGFloat {
        return 16
    }
    
    /// Cell Top Date = 24.12.2024 Height
    public func cellTopLabelHeight(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> CGFloat {
        let previousIndex = indexPath.section - 1
        if previousIndex >= 0 {
            let previousMessage = viewModel.message(at: previousIndex)
            if Calendar.current.isDate(message.sentDate, inSameDayAs: previousMessage.sentDate) {
                return .zero
            }
        }
        return 30
    }
    
    func avatarSize(
        for message: any MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> CGSize? {
        return CGSize(width: 32, height: 32)
    }
    
    func customCellSizeCalculator(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> CellSizeCalculator {
        return FileMessageCellSizeCalculator(layout: messagesCollectionView.messagesCollectionViewFlowLayout)
    }
    
    func messageStyle(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> MessageStyle {
        let corner: MessageStyle.TailCorner = isFromCurrentSender(message: message) ? .bottomRight : .bottomLeft
        return .bubbleTail(corner, .curved)
    }
}

// MARK: - MessagesDisplayDelegate

extension ChatViewController: MessagesDisplayDelegate {
    
    public func configureAvatarView(
        _ avatarView: AvatarView,
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) {
        if !isFromCurrentSender(message: message) {
            let partner = viewModel.users?.first(where: { $0.id == message.sender.senderId })
            if let image = partner?.imageUrl {
                avatarView.setImage(with: image, placeholder: .errorUserImage)
            } else {
                avatarView.image = .errorUserImage
            }
        } else {
            if let image = viewModel.currentUser.image {
                avatarView.setImage(with: image, placeholder: .errorUserImage)
            } else {
                avatarView.image = .errorUserImage
            }
        }
        avatarView.backgroundColor = .clear
        avatarView.isHidden = false
    }
    
    public func backgroundColor(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> UIColor {
        return isFromCurrentSender(message: message) ? ColorName.green200.color : .white
    }
    
    public func textColor(
        for message: MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> UIColor {
        return ColorName.contentStrong900.color
    }
    
    public func enabledDetectors(
        for message: any MessageType,
        at indexPath: IndexPath,
        in messagesCollectionView: MessagesCollectionView
    ) -> [DetectorType] {
        return [.url, .phoneNumber, .hashtag, .mention]
    }
    
    public func detectorAttributes(
        for detector: DetectorType,
        and message: MessageType,
        at indexPath: IndexPath
    ) -> [NSAttributedString.Key: Any] {
        let isFromCurrentSender = isFromCurrentSender(message: message)
        
        switch detector {
        case .url:
            return [
                .foregroundColor: isFromCurrentSender ? UIColor.white : UIColor.blue,
                .underlineStyle: NSUnderlineStyle.single.rawValue
            ]
        default:
            return MessageLabel.defaultAttributes
        }
    }
}

// MARK: - MessageCellDelegate

extension ChatViewController: MessageCellDelegate {
    
    func didSelectPhoneNumber(_ phoneNumber: String) {
        if let url = URL(string: "tel://\(phoneNumber)") {
            UIApplication.shared.open(url)
        }
    }
    
    func didSelectURL(_ url: URL) {
        UIApplication.shared.open(url)
    }
    
    public func didTapMessage(in cell: MessageCollectionViewCell) {
        guard let indexPath = messagesCollectionView.indexPath(for: cell) else { return }
        let message = viewModel.message(at: indexPath.section)
        
        if case .custom(let data) = message.kind,
           case _ = data as? FileMessageData {
            return
        }
    }
    
    func didTapAvatar(in cell: MessageCollectionViewCell) {
        guard let indexPath = messagesCollectionView.indexPath(for: cell) else { return }
        let message = viewModel.message(at: indexPath.section)
        guard !isFromCurrentSender(message: message) else { return }
        
        if let user = viewModel.users?.first(where: { $0.id == message.sender.senderId }) {
            DispatchQueue.main.async { [weak self] in
                let isGroup = self?.viewModel.chatPartner.isGroup ?? false
                self?.chatCoordinatorDelegate?.navigateToProfile(
                    userId: isGroup ? user.id : self?.viewModel.chatPartner.toUserId ?? "",
                    userName: user.username,
                    isGroup: false,
                    groupId: self?.viewModel.chatPartner.senderId
                )
            }
        }
    }
    
    func didTapImage(in cell: MessageCollectionViewCell) {
        guard let indexPath = messagesCollectionView.indexPath(for: cell) else { return }
        let message = viewModel.message(at: indexPath.section)
        if case .photo(let media) = message.kind, let image = media.image {
            presentImageFullscreen(image: image)
        } else if case .video(let videoItem) = message.kind, let url = videoItem.url {
            playVideo(from: url)
        }
    }
}

// MARK: - ChatInputBarDelegate

extension ChatViewController: ChatInputBarDelegate {
    func inputBar(_ inputBar: ChatInputBar, didSendMessage message: String) {
        viewModel.sendTextMessage(message)
    }
    
    func inputBar(_ inputBar: ChatInputBar, isTyping: Bool) {
        if isTyping {
            viewModel.notifyTyping()
        }
    }

    func didTappedCameraButton() {
        PermissionManager.shared.checkPermission(.camera) { [weak self] status in
            guard let self = self else { return }
            switch status {
            case .authorized:
                self.presentImagePicker(sourceType: .camera)
            case .denied, .restricted:
                self.showPermissionAlert(
                    title: L10n.Chat.Error.cameraPermissionTitle,
                    message: L10n.Chat.Error.cameraPermissionMessage
                )
            case .notDetermined:
                self.requestCameraPermission()
            }
        }
    }
    
    func didTappedGallerryButton() {
        PermissionManager.shared.checkPermission(.photoLibrary) { [weak self] status in
            guard let self = self else { return }
            switch status {
            case .authorized:
                self.presentImagePicker(
                    sourceType: .photoLibrary,
                    mediaTypes: ["public.image", "public.movie"]
                )
            case .denied, .restricted:
                self.showPermissionAlert(
                    title: L10n.Chat.Error.galleryPermissionTitle,
                    message: L10n.Chat.Error.galleryPermissionMessage
                )
            case .notDetermined:
                PermissionManager.shared.requestPermission(.photoLibrary) { granted in
                    if granted {
                        self.presentImagePicker(
                            sourceType: .photoLibrary,
                            mediaTypes: ["public.image", "public.movie"]
                        )
                    }
                }
            }
        }
    }
    
    func didTappedFileButton() {
        let documentPicker = UIDocumentPickerViewController(forOpeningContentTypes: [.pdf, .plainText, .jpeg, .png, .zip])
        documentPicker.delegate = self
        present(documentPicker, animated: true)
    }
    
    func inputBarDidBeginEditing(_ inputBar: ChatInputBar) {
        guard viewModel.messageCount > 0 else { return }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.3) { [weak self] in
            self?.messagesCollectionView.scrollToLastItem(animated: true)
        }
    }
    
    private func showPermissionAlert(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: L10n.Chat.Button.settings, style: .default) { _ in
            if let settingsURL = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(settingsURL)
            }
        })
        alert.addAction(UIAlertAction(title: L10n.Chat.Button.cancel, style: .cancel))
        present(alert, animated: true)
    }
}

// MARK: - DocumentPickerDelegate

extension ChatViewController: UIDocumentPickerDelegate {
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentsAt urls: [URL]) {
        guard let fileURL = urls.first else { return }
        if !isAllowedFileType(url: fileURL) {
            showError(message: L10n.Chat.Error.fileTypeNotSupported)
            return
        }
        var shouldStopAccessing = false
        if fileURL.startAccessingSecurityScopedResource() {
            shouldStopAccessing = true
        }
        
        defer {
            if shouldStopAccessing {
                fileURL.stopAccessingSecurityScopedResource()
            }
        }
        
        do {
            let resources = try fileURL.resourceValues(forKeys: [.fileSizeKey, .typeIdentifierKey])
            let fileSize = resources.fileSize ?? 0
            
            guard fileSize <= 50_000_000 else {
                showError(message: L10n.Chat.Error.fileSizeTooLarge)
                return
            }
            
            let cachesDirectory = FileManager.default.urls(for: .cachesDirectory, in: .userDomainMask).first!
            let uniqueFileName = "\(UUID().uuidString)_\(fileURL.lastPathComponent)"
            let destinationURL = cachesDirectory.appendingPathComponent(uniqueFileName)
            
            if FileManager.default.fileExists(atPath: destinationURL.path) {
                try FileManager.default.removeItem(at: destinationURL)
            }
            try FileManager.default.copyItem(at: fileURL, to: destinationURL)
            viewModel.sendFile(fileUrl: destinationURL)
        } catch {
            showError(message: L10n.Chat.Error.fileProcessingError(error.localizedDescription))
        }
    }
    
    private func isAllowedFileType(url: URL) -> Bool {
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
        
        do {
            let resources = try url.resourceValues(forKeys: [.typeIdentifierKey])
            if let fileType = resources.typeIdentifier, let fileUTType = UTType(fileType) {
                print("✅ Dosya Türü (typeIdentifierKey): \(fileUTType)")
                return allowedTypes.contains(fileUTType)
            }
        } catch {
            print("⚠️ typeIdentifierKey alınamadı: \(error.localizedDescription)")
        }

        let fileExtension = url.pathExtension.lowercased()
        if let fileUTType = UTType(filenameExtension: fileExtension) {
            return allowedTypes.contains(fileUTType)
        }
        return false
    }
}

// MARK: - UIImagePickerControllerDelegate

extension ChatViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    private func presentImagePicker(
        sourceType: UIImagePickerController.SourceType,
        mediaTypes: [String] = ["public.image", "public.movie"]
    ) {
        let picker = UIImagePickerController()
        picker.delegate = self
        picker.sourceType = sourceType
        picker.mediaTypes = mediaTypes
        picker.videoQuality = .typeMedium
        picker.allowsEditing = true
        picker.videoMaximumDuration = 60
        picker.modalPresentationStyle = .fullScreen
        present(picker, animated: true)
    }
    
    func imagePickerController(
        _ picker: UIImagePickerController,
        didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey: Any]
    ) {
        picker.dismiss(animated: true)
        
        if let mediaType = info[.mediaType] as? String {
            switch mediaType {
            case "public.image":
                if let image = info[.editedImage] as? UIImage {
                    viewModel.sendImage(image, imageType: ".png")
                }
                
            case "public.movie":
                if let videoURL = info[.mediaURL] as? URL {
                    let filePathExtension = videoURL.pathExtension
                    do {
                        let resources = try videoURL.resourceValues(forKeys: [.fileSizeKey])
                        let fileSize = resources.fileSize ?? 0
                        
                        guard fileSize <= 10_000_000 else {
                            showError(message: L10n.Chat.Error.videoSizeTooLarge)
                            return
                        }
                        viewModel.sendVideo(url: videoURL, videoType: filePathExtension)
                    } catch {
                        showError(message: L10n.Chat.Error.videoProcessingError)
                    }
                }
            default:
                break
            }
        }
    }
}

// MARK: - Permission Handling

extension ChatViewController {
    private func requestCameraPermission() {
        PermissionManager.shared.requestPermission(.camera) { [weak self] granted in
            DispatchQueue.main.async {
                if granted {
                    self?.presentImagePicker(sourceType: .camera)
                } else {
                    self?.showPermissionAlert(
                        title: L10n.Chat.Error.cameraPermissionTitle,
                        message: L10n.Chat.Error.cameraPermissionMessage
                    )
                }
            }
        }
    }
    
    private func showError(message: String) {
        let alert = UIAlertController(
            title: L10n.Chat.Error.title,
            message: message,
            preferredStyle: .alert
        )
        alert.addAction(UIAlertAction(title: L10n.Chat.Button.ok, style: .default))
        present(alert, animated: true)
    }
}

// MARK: - MessagesCollectionView DataSourcePrefetching

extension ChatViewController: UICollectionViewDataSourcePrefetching {
    func collectionView(
        _ collectionView: UICollectionView,
        prefetchItemsAt indexPaths: [IndexPath]
    ) {
        guard let firstIndexPath = indexPaths.first,
              firstIndexPath.section < 9,
              !refreshControl.isRefreshing,
              viewModel.hasMoreMessages
        else { return }
        
        loadMoreMessages()
    }
    
    
    private func loadMoreMessages() {
        showRefreshControl()
        
        viewModel.loadPreviousMessages { [weak self] isSuccess in
            guard let self = self else { return }
            
            DispatchQueue.main.async {
                self.refreshControl.endRefreshing()
                
                if isSuccess {
                    self.messagesCollectionView.reloadDataAndKeepOffset()
                } else {
                    self.hideRefreshControl()
                }
            }
        }
    }
    
    private func showRefreshControl() {
        guard !refreshControl.isRefreshing else { return }
        
        if messagesCollectionView.refreshControl == nil {
            messagesCollectionView.refreshControl = refreshControl
        }
        refreshControl.beginRefreshing()
    }
    
    private func hideRefreshControl() {
        refreshControl.endRefreshing()
        messagesCollectionView.refreshControl = nil
    }
    
}

// MARK: - ScrollViewDidScroll

extension ChatViewController {
    override func scrollViewDidScroll(_ scrollView: UIScrollView) {
        super.scrollViewDidScroll(scrollView)
        
        let threshold: CGFloat = 100
        let contentOffset = scrollView.contentOffset.y
        let contentHeight = scrollView.contentSize.height
        let frameHeight = scrollView.frame.size.height
        
        let shouldShowButton = contentOffset < (contentHeight - frameHeight - threshold)
        
        UIView.animate(withDuration: 0.3) {
            self.scrollToBottomButton.alpha = shouldShowButton ? 1.0 : 0.0
            self.scrollToBottomButton.isHidden = !shouldShowButton
        }
    }
}

extension ChatViewController: ChatNavigationTitleDelegate {
    func didTapNavigationTitle() {
        self.chatCoordinatorDelegate?.navigateToProfile(
            userId: self.viewModel.chatPartner.toUserId ?? "",
            userName: self.viewModel.chatPartner.displayName,
            isGroup: self.viewModel.chatPartner.isGroup ?? false,
            groupId: self.viewModel.chatPartner.senderId
        )
    }
}
