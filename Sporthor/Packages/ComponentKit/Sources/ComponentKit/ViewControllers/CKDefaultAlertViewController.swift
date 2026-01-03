//
//  CKDefaultAlertViewController.swift
//  ComponentKit
//
//  Created by derTurke on 18.07.2025.
//


import UIKit
import DesignKit

public final class CKDefaultAlertViewController: UIViewController {

    private weak var delegate: CKDefaultAlertDelegate?

    private let titleText: String
    private let messageText: String
    private let okTitle: String
    private let cancelTitle: String?

    // MARK: - Init
    public init(delegate: CKDefaultAlertDelegate? = nil,
                title: String,
                message: String,
                okTitle: String = "Tamam",
                cancelTitle: String? = "İptal") {
        self.delegate = delegate
        self.titleText = title
        self.messageText = message
        self.okTitle = okTitle
        self.cancelTitle = cancelTitle
        super.init(nibName: nil, bundle: nil)
        modalPresentationStyle = .overFullScreen
        modalTransitionStyle = .crossDissolve
    }

    public required init?(coder: NSCoder) {
        self.titleText = ""
        self.messageText = ""
        self.okTitle = ""
        self.cancelTitle = nil
        super.init(coder: coder)
    }

    // MARK: - Views
    private let backgroundView: UIView = {
        let view = UIView()
        view.backgroundColor = UIColor.black.withAlphaComponent(0.4)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private let containerView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 16
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private let titleLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentStrong900.color,
            numberOfLines: 0,
            textAlignment: .center,
            font: .heading06
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()

    private let messageLabel: CKLabel = {
        let label = CKLabel(
            textColor: ColorName.contentSub800.color,
            numberOfLines: 0,
            textAlignment: .center,
            font: .body04Compact
        )
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()

    private let okButton: CKButton = {
        let button = CKButton(
            titleColor: ColorName.contentStrong900.color,
            buttonBackgroundColor: ColorName.backgroundPrimaryGreen.color,
            cornerRadius: 8,
            font: .bold03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()

    private let cancelButton: UIButton = {
        let button = CKButton(
            titleColor: ColorName.contentStrong900.color,
            buttonBackgroundColor: .clear,
            cornerRadius: 8,
            font: .body03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()

    // MARK: - Lifecycle
    public override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
        configureContent()
    }

    // MARK: - Setup
    private func setupUI() {
        view.addSubview(backgroundView)
        view.addSubview(containerView)

        containerView.addSubview(titleLabel)
        containerView.addSubview(messageLabel)
        containerView.addSubview(okButton)

        NSLayoutConstraint.activate([
            backgroundView.topAnchor.constraint(equalTo: view.topAnchor),
            backgroundView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            backgroundView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            backgroundView.trailingAnchor.constraint(equalTo: view.trailingAnchor),

            containerView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            containerView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 40),
            containerView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -40),

            titleLabel.topAnchor.constraint(equalTo: containerView.topAnchor, constant: 20),
            titleLabel.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),

            messageLabel.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 12),
            messageLabel.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            messageLabel.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),

            okButton.topAnchor.constraint(equalTo: messageLabel.bottomAnchor, constant: 20),
            okButton.leadingAnchor.constraint(equalTo: containerView.leadingAnchor, constant: 16),
            okButton.trailingAnchor.constraint(equalTo: containerView.trailingAnchor, constant: -16),
            okButton.heightAnchor.constraint(equalToConstant: 44)
        ])

        okButton.addTarget(self, action: #selector(okTapped), for: .touchUpInside)

        if let cancelTitle = cancelTitle,
           !cancelTitle.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {

            containerView.addSubview(cancelButton)

            NSLayoutConstraint.activate([
                cancelButton.topAnchor.constraint(equalTo: okButton.bottomAnchor, constant: 8),
                cancelButton.centerXAnchor.constraint(equalTo: containerView.centerXAnchor),
                cancelButton.bottomAnchor.constraint(equalTo: containerView.bottomAnchor, constant: -12)
            ])

            cancelButton.setTitle(cancelTitle, for: .normal)
            cancelButton.addTarget(self, action: #selector(cancelTapped), for: .touchUpInside)
        } else {
            NSLayoutConstraint.activate([
                okButton.bottomAnchor.constraint(equalTo: containerView.bottomAnchor, constant: -20)
            ])
        }
    }

    private func configureContent() {
        titleLabel.text = titleText
        messageLabel.text = messageText
        okButton.setTitle(okTitle, for: .normal)
    }

    // MARK: - Actions
    @objc private func okTapped() {
        dismiss(animated: true) {
            self.delegate?.ckDefaultAlertDidTapOK()
        }
    }

    @objc private func cancelTapped() {
        dismiss(animated: true) {
            self.delegate?.ckDefaultAlertDidTapCancel()
        }
    }
}
