//
//  SuccessTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit
import ComponentKit

final class SuccessTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SuccessTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! SuccessTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var topBackgroundView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.headerGradient.image
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.clipsToBounds = true
        return imageView
    }()
    
    private lazy var topBackgroundSuccessImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.image = Asset.celebrationBlurBackground.image
        imageView.contentMode = .scaleToFill
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.clipsToBounds = true
        return imageView
    }()
    
    private lazy var clubImageBorderView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.setCornerRadius(36)
        view.setBorderWidth(1)
        view.setBorderColor(DesignKitColorName.borderSoft200.color)
        view.clipsToBounds = true
        view.translatesAutoresizingMaskIntoConstraints = false
        view.widthAnchor.constraint(equalToConstant: 72).isActive = true
        view.heightAnchor.constraint(equalToConstant: 72).isActive = true
        return view
    }()
    
    private lazy var clubImageView: UIImageView = {
        let imageView = UIImageView()
        imageView.setCornerRadius(28)
        imageView.clipsToBounds = true
        imageView.translatesAutoresizingMaskIntoConstraints = false
        imageView.widthAnchor.constraint(equalToConstant: 56).isActive = true
        imageView.heightAnchor.constraint(equalToConstant: 56).isActive = true
        return imageView
    }()
    
    private lazy var clubNameLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentWhite0.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .bold03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var clubTeamLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentDisable300.color,
                            numberOfLines: 0,
                            textAlignment: .center,
                            font: .bold03Compact)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var bottomView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundWhite0.color
        view.translatesAutoresizingMaskIntoConstraints = false
        view.layer.cornerRadius = 32
        view.layer.maskedCorners = [.layerMinXMinYCorner, .layerMaxXMinYCorner]
        view.layer.masksToBounds = true
        return view
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
                              title: "Grup Üyelerini Davet Et",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 0)
        button.translatesAutoresizingMaskIntoConstraints = false
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - SuccessTrainingGroupPresenterDelegate
extension SuccessTrainingGroupViewController: SuccessTrainingGroupPresenterDelegate {
    func prepareUI() {
        clubImageBorderView.addSubview(clubImageView)
        topBackgroundView.addSubview(topBackgroundSuccessImageView)
        topBackgroundView.addSubview(clubImageBorderView)
        topBackgroundView.addSubview(clubNameLabel)
        topBackgroundView.addSubview(clubTeamLabel)
        view.addSubview(topBackgroundView)
        
        bottomView.addSubview(successHeaderLabel)
        bottomView.addSubview(successDescriptionLabel)
        bottomView.addSubview(continueButton)
        
        view.addSubview(bottomView)
        let screenWidth: CGFloat = UIScreen.main.bounds.width
        
        NSLayoutConstraint.activate([
            topBackgroundView.topAnchor.constraint(equalTo: view.topAnchor),
            topBackgroundView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            topBackgroundView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            topBackgroundView.heightAnchor.constraint(equalToConstant: screenWidth),
            
            topBackgroundSuccessImageView.topAnchor.constraint(equalTo: topBackgroundView.topAnchor),
            topBackgroundSuccessImageView.leadingAnchor.constraint(equalTo: topBackgroundView.leadingAnchor),
            topBackgroundSuccessImageView.trailingAnchor.constraint(equalTo: topBackgroundView.trailingAnchor),
            topBackgroundSuccessImageView.bottomAnchor.constraint(equalTo: topBackgroundView.bottomAnchor),
            
            clubImageBorderView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 54),
            clubImageBorderView.centerXAnchor.constraint(equalTo: topBackgroundView.centerXAnchor),
            
            clubImageView.centerXAnchor.constraint(equalTo: clubImageBorderView.centerXAnchor),
            clubImageView.centerYAnchor.constraint(equalTo: clubImageBorderView.centerYAnchor),
            
            clubNameLabel.topAnchor.constraint(equalTo: clubImageBorderView.bottomAnchor, constant: 6),
            clubNameLabel.leadingAnchor.constraint(equalTo: topBackgroundView.leadingAnchor, constant: 24),
            clubNameLabel.trailingAnchor.constraint(equalTo: topBackgroundView.trailingAnchor, constant: -24),
            
            clubTeamLabel.topAnchor.constraint(equalTo: clubNameLabel.bottomAnchor, constant: 4),
            clubTeamLabel.leadingAnchor.constraint(equalTo: topBackgroundView.leadingAnchor, constant: 24),
            clubTeamLabel.trailingAnchor.constraint(equalTo: topBackgroundView.trailingAnchor, constant: -24),
            
            bottomView.topAnchor.constraint(equalTo: clubTeamLabel.bottomAnchor, constant: 32),
            bottomView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            bottomView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            bottomView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            successHeaderLabel.topAnchor.constraint(equalTo: bottomView.topAnchor, constant: 42),
            successHeaderLabel.leadingAnchor.constraint(equalTo: bottomView.leadingAnchor, constant: 24),
            successHeaderLabel.trailingAnchor.constraint(equalTo: bottomView.trailingAnchor, constant: -24),
            
            successDescriptionLabel.topAnchor.constraint(equalTo: successHeaderLabel.bottomAnchor, constant: 16),
            successDescriptionLabel.leadingAnchor.constraint(equalTo: bottomView.leadingAnchor, constant: 24),
            successDescriptionLabel.trailingAnchor.constraint(equalTo: bottomView.trailingAnchor, constant: -24),
            
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            continueButton.leadingAnchor.constraint(equalTo: bottomView.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: bottomView.trailingAnchor, constant: -24)
        ])
    }
    
    func prepareTeam(image: String, name: String, groupName: String) {
        clubImageView.setImage(with: image)
        clubNameLabel.text = name
        clubTeamLabel.text = groupName
    }
    
    func prepareSuccessHeaderAndDescription(header: String, description: String) {
        successHeaderLabel.text = header
        successDescriptionLabel.text = description
    }
}

extension SuccessTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton(tag)
    }
}
