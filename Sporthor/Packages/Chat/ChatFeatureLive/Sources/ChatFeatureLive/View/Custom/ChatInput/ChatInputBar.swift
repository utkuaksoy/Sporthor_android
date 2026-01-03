//
//  ChatInputBar.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 28.01.2025.
//

import DesignKit
import UIKit
import InputBarAccessoryView

public enum CustomAttachmentType: Int, CaseIterable {
    case camera = 0
    case gallery
    case file
}

struct CustomAttachment {
    let type: CustomAttachmentType
    let icon: UIImage
    let title: String
}

protocol ChatInputBarDelegate: AnyObject {
    func inputBar(_ inputBar: ChatInputBar, didSendMessage message: String)
    func didTappedCameraButton()
    func didTappedGallerryButton()
    func didTappedFileButton()
    func inputBar(_ inputBar: ChatInputBar, isTyping: Bool)
    func inputBarDidBeginEditing(_ inputBar: ChatInputBar)
}

final class ChatInputBar: InputBarAccessoryView {

    // MARK: - UI Elements

    private lazy var addButton: InputBarButtonItem = makeAddButton()
    private lazy var customSendButton: InputBarButtonItem = makeSendButton()
    
    private lazy var collectionContainerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        layout.minimumInteritemSpacing = 8
        layout.minimumLineSpacing = 8
        let view = UICollectionView(frame: .zero, collectionViewLayout: layout)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.backgroundColor = .white
        view.showsHorizontalScrollIndicator = false
        return view
    }()

    private var attachments: [CustomAttachment] = []
    weak var chatDelegate: ChatInputBarDelegate?

    private var isAttachmentVisible = false {
        didSet { updateAddButtonAppearance(isKeyboardButton: isAttachmentVisible) }
    }

    private var keyboardHeight: CGFloat = 291
    private var collectionViewHeightConstraint: NSLayoutConstraint?

    private var typingWorkItem: DispatchWorkItem?

    override init(frame: CGRect) {
        super.init(frame: frame)
        configureViews()
        setupConstraints()
        observeKeyboardNotifications()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: - View Setup

    private func configureViews() {
        backgroundColor = .white
        backgroundView.backgroundColor = .clear
        separatorLine.isHidden = true

        configureStackViews()
        configurePadding()
        configureInputTextView()
        configureAttachmentData()
        configureAttachmentCollectionView()
    }

    private func configureStackViews() {
        setLeftStackViewWidthConstant(to: 40, animated: false)
        setStackViewItems([addButton], forStack: .left, animated: false)
        setRightStackViewWidthConstant(to: 40, animated: false)
        setStackViewItems([customSendButton], forStack: .right, animated: false)

        topStackView.alignment = .center
        topStackView.distribution = .fill
        topStackView.spacing = 0
        topStackView.isHidden = false
    }

    private func configurePadding() {
        padding = UIEdgeInsets(top: 8, left: 8, bottom: 8, right: 8)
        middleContentViewPadding.right = 8
        middleContentViewPadding.left = 8
    }

    private func configureInputTextView() {
        inputTextView.placeholder = L10n.Chat.TextField.Hint.title
        inputTextView.placeholderTextColor = ColorName.borderSoft200.color
        inputTextView.textColor = ColorName.contentStrong900.color
        inputTextView.font = .bold04Compact
        inputTextView.backgroundColor = ColorName.backgroundWeak100.color
        inputTextView.layer.cornerRadius = 18
        inputTextView.layer.borderWidth = 1
        inputTextView.layer.borderColor = ColorName.borderSoft200.color.cgColor
        inputTextView.textContainerInset = UIEdgeInsets(top: 10, left: 12, bottom: 10, right: 12)
        inputTextView.delegate = self
        inputTextView.sizeToFit()
        self.delegate = self
    }

    private func configureAttachmentData() {
        attachments = [
            .init(type: .camera, icon: .chatCameraIcon, title: "Kamera"),
            .init(type: .gallery, icon: .chatLibraryIcon, title: "Fotoğraflar"),
            .init(type: .file, icon: .arrowRightIcon, title: "Belge")
        ]
    }

    private func configureAttachmentCollectionView() {
        bottomStackView.axis = .horizontal
        bottomStackView.alignment = .fill
        bottomStackView.distribution = .fill
        bottomStackView.addArrangedSubview(collectionContainerView)
        collectionContainerView.addSubview(collectionView)

        let heightConstraint = collectionContainerView.heightAnchor.constraint(equalToConstant: keyboardHeight - 34)
        heightConstraint.isActive = true
        collectionViewHeightConstraint = heightConstraint

        collectionView.register(AttachmentOptionCell.self, forCellWithReuseIdentifier: AttachmentOptionCell.reuseIdentifier)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionContainerView.isHidden = true
    }

    // MARK: - Keyboard Observer

    private func observeKeyboardNotifications() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(keyboardWillShow(_:)),
            name: UIResponder.keyboardWillShowNotification,
            object: nil
        )
    }

    @objc
    private func keyboardWillShow(_ notification: Notification) {
        UIView.performWithoutAnimation {
            if let keyboardFrame = notification.userInfo?[UIResponder.keyboardFrameEndUserInfoKey] as? CGRect {
                keyboardHeight = keyboardFrame.height
            }
        }
    }

    // MARK: - Actions

    private func didPressSendButtonWith(text: String) {
        let trimmedText = text.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmedText.isEmpty else { return }
        dismissAttachmentView()
        chatDelegate?.inputBar(self, didSendMessage: trimmedText)
        inputTextView.text = ""
        updateButtonVisibility(for: "")
    }

    func toggleAttachmentManager() {
        isAttachmentVisible.toggle()

        if isAttachmentVisible {
            UIView.performWithoutAnimation {
                inputTextView.resignFirstResponder()
                collectionViewHeightConstraint?.constant = (keyboardHeight - 34)
                collectionContainerView.isHidden = false
                collectionView.reloadData()
                collectionView.layoutIfNeeded()
            }
            updateAddButtonAppearance(isKeyboardButton: true)
        } else {
            UIView.performWithoutAnimation {
                collectionContainerView.isHidden = true
                inputTextView.becomeFirstResponder()
            }
            updateAddButtonAppearance(isKeyboardButton: false)
        }
    }

    func updateAddButtonAppearance(isKeyboardButton: Bool) {
        guard let attachmentButton = leftStackView.arrangedSubviews.first as? InputBarButtonItem else { return }
        attachmentButton.setImage(
            UIImage(systemName: isKeyboardButton ? "keyboard" : "plus"),
            for: .normal
        )
    }

    // MARK: - Factory

    private func makeAddButton() -> InputBarButtonItem {
        let item = InputBarButtonItem()
        item.setSize(CGSize(width: 38, height: 38), animated: false)
        item.image = UIImage(systemName: "plus")
        item.tintColor = ColorName.contentStrong900.color
        item.layer.cornerRadius = 19
        item.backgroundColor = .clear
        item.layer.borderWidth = 1
        item.layer.borderColor = ColorName.borderSoft200.color.cgColor
        item.clipsToBounds = true
        item.onTouchUpInside { [weak self] _ in self?.toggleAttachmentManager() }
        return item
    }

    private func makeSendButton() -> InputBarButtonItem {
        let item = InputBarButtonItem()
        item.setSize(CGSize(width: 38, height: 38), animated: false)
        item.image = UIImage(systemName: "arrow.up")
        item.backgroundColor = ColorName.backgroundSub300.color
        item.layer.cornerRadius = 19
        item.tintColor = ColorName.contentStrong900.color
        item.onTouchUpInside { [weak self] _ in
            guard let self = self else { return }
            self.didPressSendButtonWith(text: self.inputTextView.text)
        }
        return item
    }
    
    func dismissAttachmentView() {
        UIView.performWithoutAnimation {
            isAttachmentVisible = false
            collectionContainerView.isHidden = true
            updateAddButtonAppearance(isKeyboardButton: false)
            inputTextView.becomeFirstResponder()
        }
    }
    
    func dimissKeyboard() {
        UIView.performWithoutAnimation {
            isAttachmentVisible = false
            collectionContainerView.isHidden = true
            updateAddButtonAppearance(isKeyboardButton: false)
            inputTextView.resignFirstResponder()
        }
    }
}

// MARK: - InputBarAccessoryViewDelegate

extension ChatInputBar: InputBarAccessoryViewDelegate {
    func inputBar(_ inputBar: InputBarAccessoryView, textViewTextDidChangeTo text: String) {
        let trimmedText = text.trimmingCharacters(in: .whitespacesAndNewlines)
        updateButtonVisibility(for: trimmedText)
        chatDelegate?.inputBar(self, isTyping: true)
    }
}

// MARK: - UITextViewDelegate

extension ChatInputBar: UITextViewDelegate {
    public func textViewDidBeginEditing(_ textView: UITextView) {
        if isAttachmentVisible {
            dismissAttachmentView()
        }
        chatDelegate?.inputBarDidBeginEditing(self)
    }
}

// MARK: - UICollectionView DataSource & Layout

extension ChatInputBar: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return attachments.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: AttachmentOptionCell.reuseIdentifier,
            for: indexPath
        ) as? AttachmentOptionCell else {
            return UICollectionViewCell()
        }
        cell.configure(icon: attachments[indexPath.row].icon, title: attachments[indexPath.row].title)
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = (UIScreen.main.bounds.width - 48) / 3
        return CGSize(width: width, height: 78)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let type = attachments[indexPath.row].type
        
        UIView.performWithoutAnimation {
            inputTextView.resignFirstResponder()
            collectionContainerView.isHidden = true
            isAttachmentVisible = false
            updateAddButtonAppearance(isKeyboardButton: false)
        }
        
        switch type {
        case .camera:
            chatDelegate?.didTappedCameraButton()
        case .gallery:
            chatDelegate?.didTappedGallerryButton()
        case .file:
            chatDelegate?.didTappedFileButton()
        }
    }
}

// MARK: - Helpers

private extension ChatInputBar {
    func updateButtonVisibility(for text: String) {
        let hasText = !text.isEmpty
        UIView.animate(withDuration: 0.3) {
            self.customSendButton.backgroundColor = hasText ? ColorName.backgroundPrimaryGreen.color : ColorName.backgroundSub300.color
        }
    }
}

private extension ChatInputBar {
    
    func setupConstraints() {
        NSLayoutConstraint.activate([
            collectionView.leadingAnchor.constraint(equalTo: collectionContainerView.leadingAnchor, constant: 8),
            collectionView.trailingAnchor.constraint(equalTo: collectionContainerView.trailingAnchor, constant: -8),
            collectionView.topAnchor.constraint(equalTo: collectionContainerView.topAnchor, constant: 8),
            collectionView.heightAnchor.constraint(equalToConstant: 86)
        ])
    }
}
