//
//  SuccessCreateClubViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import ComponentKit
import PanModal

final class SuccessCreateClubViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SuccessCreateClubPresenterProtocol {
        get { return self.basePresenter as! SuccessCreateClubPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var celebrationImageView: UIImageView = {
        let imageView = UIImageView(image: Asset.celebrationBackground.image)
        imageView.translatesAutoresizingMaskIntoConstraints = false
        return imageView
    }()
    
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var imageBorderView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.setCornerRadius(36)
        view.setBorderColor(DesignKitColorName.borderSoft200.color)
        view.setBorderWidth(1)
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        view.widthAnchor.constraint(equalToConstant: 72).isActive = true
        view.heightAnchor.constraint(equalToConstant: 72).isActive = true
        return view
    }()
    
    private lazy var imageView: UIImageView = {
        let imageView = UIImageView()
        imageView.setCornerRadius(31)
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 62).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 62).isActive = true
        return imageView
    }()
    
    private lazy var clubNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0, textAlignment: .center,
                            font: .heading07)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var successHeaderLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .heading05)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var successDescriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSub800.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .body04Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Yetki Belgesi Gönder",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 0)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var skipButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Bu Adımı Atla",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: .clear,
                              cornerRadius: 23,
                              borderWidth: 1,
                              borderColor: DesignKitColorName.contentStrong900.color,
                              font: .bold03Compact,
                              tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var buttonsStackView: CKStackView = {
        let stackView = CKStackView(spacing: 12)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        stackView.addArrangedSubviews([continueButton, skipButton])
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - SuccessCreateClubPresenterDelegate
extension SuccessCreateClubViewController: SuccessCreateClubPresenterDelegate {
    func prepareUI() {
        view.addSubview(celebrationImageView)
        view.addSubview(scrollLineView)
        imageBorderView.addSubview(imageView)
        view.addSubview(imageBorderView)
        view.addSubview(clubNameLabel)
        view.addSubview(successHeaderLabel)
        view.addSubview(successDescriptionLabel)
        view.addSubview(buttonsStackView)
        
        NSLayoutConstraint.activate([
            celebrationImageView.topAnchor.constraint(equalTo: view.topAnchor),
            celebrationImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            celebrationImageView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            celebrationImageView.heightAnchor.constraint(equalTo: view.widthAnchor),
            
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            imageBorderView.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 52),
            imageBorderView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            
            imageView.centerYAnchor.constraint(equalTo: imageBorderView.centerYAnchor),
            imageView.centerXAnchor.constraint(equalTo: imageBorderView.centerXAnchor),
            
            clubNameLabel.topAnchor.constraint(equalTo: imageBorderView.bottomAnchor, constant: 10),
            clubNameLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            clubNameLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            successHeaderLabel.topAnchor.constraint(equalTo: clubNameLabel.bottomAnchor, constant: 32),
            successHeaderLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            successHeaderLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            successDescriptionLabel.topAnchor.constraint(equalTo: successHeaderLabel.bottomAnchor, constant: 12),
            successDescriptionLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            successDescriptionLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            buttonsStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            buttonsStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            buttonsStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32)
        ])
    }
    
    func prepareTeam(image: String, name: String) {
        imageView.setImage(with: image)
        clubNameLabel.text = name
    }
    
    func prepareSuccessHeaderAndDescription(header: String, description: String) {
        successHeaderLabel.text = header
        successDescriptionLabel.text = description
    }
}

// MARK: - PanModalPresentable
extension SuccessCreateClubViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
    
    var longFormHeight: PanModalHeight {
        .maxHeightWithTopInset(150)
    }
}

// MARK: - CKButtonDelegate
extension SuccessCreateClubViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}
