//
//  ExperienceMainViewController.swift
//  Sporthor
//
//  Created by derTurke on 17.02.2025.
//
//

import UIKit
import ComponentKit

final class ExperienceMainViewController: BaseViewController {
    
    // MARK: - VIPER Variables
    var presenter: ExperienceMainPresenterProtocol {
        get { return self.basePresenter as! ExperienceMainPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var gradientView: CKGradientView = {
        let gradientView = CKGradientView(colors: [DesignKitColorName.successLighter100.color, .white])
        gradientView.translatesAutoresizingMaskIntoConstraints = false
        return gradientView
    }()
    
    private lazy var progressView: CKProgressView = {
        let progressView = CKProgressView(steps: 4, selectedStep: 1)
        progressView.translatesAutoresizingMaskIntoConstraints = false
        return progressView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color,
                            numberOfLines: 0,
                            font: .heading04)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact
        )
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: true)
    }
    
    // MARK: - Custom Methods
}

// MARK: - ExperienceMainPresenterDelegate
extension ExperienceMainViewController: ExperienceMainPresenterDelegate {
    
    func prepareUI() {
        view.addSubview(gradientView)
        gradientView.addSubview(progressView)
        gradientView.addSubview(titleLabel)
        gradientView.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            gradientView.topAnchor.constraint(equalTo: view.topAnchor),
            gradientView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            gradientView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            gradientView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            progressView.topAnchor.constraint(equalTo: gradientView.safeAreaLayoutGuide.topAnchor),
            progressView.leadingAnchor.constraint(equalTo: gradientView.leadingAnchor, constant: 24),
            progressView.trailingAnchor.constraint(equalTo: gradientView.trailingAnchor, constant: -24),
            
            titleLabel.topAnchor.constraint(equalTo: progressView.bottomAnchor, constant: 41),
            titleLabel.leadingAnchor.constraint(equalTo: progressView.leadingAnchor),
            titleLabel.trailingAnchor.constraint(equalTo: progressView.trailingAnchor),
            
            continueButton.leadingAnchor.constraint(equalTo: progressView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: progressView.trailingAnchor),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
        ])
    }
    
    func didSetMainTitle(_ title: String) {
        titleLabel.text = title
    }
    
    func didSetContinueButttonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
}

extension ExperienceMainViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedButton(with: tag)
    }
}
