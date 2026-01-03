//
//  CKPostCommentView.swift
//  ComponentKit
//
//  Created by derTurke on 29.03.2025.
//

import UIKit
import DesignKit

public final class CKPostCommentView: UIView {
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(alignment: .leading, spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: CKPostCommentViewDelegate?
    private var comments: [(username: String, comment: String, userId: String)] = []
    
    // MARK: - Initializers
    public override init(frame: CGRect) {
        super.init(frame: frame)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    private func setupUI() {
        addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKPostCommentViewDelegate? = nil,
                     comments: [(username: String, comment: String, userId: String)]) {
        self.delegate = delegate
        self.comments = comments
        stackView.removeAllArrangedSubviews()
        comments.enumerated().forEach { index, comment in
            let label = CKLabel(textColor: DesignKit.ColorName.contentSub800.color,
                                numberOfLines: 0,
                                isUserInteractionEnabled: true,
                                tag: index)
            let attributedText = NSMutableAttributedString(
                string: "\(comment.username) ",
                attributes: [.font: UIFont.bold04Compact]
            )
            attributedText.append(NSAttributedString(
                string: comment.comment,
                attributes: [.font: UIFont.body04Compact]
            ))
            label.attributedText = attributedText
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTapComment(_:)))
            label.addGestureRecognizer(tapGesture)
            stackView.addArrangedSubview(label)
        }
    }
    
    @objc private func handleTapComment(_ gesture: UITapGestureRecognizer) {
        guard let label = gesture.view as? CKLabel else { return }
        let comment = comments[label.tag]
        label.detectTap(on: comment.username, gesture: gesture, completion: { [weak self] in
            guard let self,
                  let delegate = delegate,
                  !comment.userId.isEmpty else { return }
            delegate.didTappedUsernameInCommentView(comment.username, userId: comment.userId)
        })
        
    }
}

