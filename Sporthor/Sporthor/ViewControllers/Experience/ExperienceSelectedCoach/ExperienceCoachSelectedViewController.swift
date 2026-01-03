//
//  ExperienceCoachSelectedViewController.swift
//  Sporthor
//
//  Created by derTurke on 16.05.2025.
//
//

import UIKit
import ComponentKit

final class ExperienceCoachSelectedViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: ExperienceCoachSelectedPresenterProtocol {
        get { return self.basePresenter as! ExperienceCoachSelectedPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var progressView: CKProgressView = {
        let progressView = CKProgressView(steps: 5, selectedStep: 4)
        progressView.translatesAutoresizingMaskIntoConstraints = false
        return progressView
    }()
    
    private lazy var textStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentStrong900.color, numberOfLines: 0, font: .heading04)
        return label
    }()
    
    private lazy var descriptionLabel: CKLabel = {
        let label = CKLabel(textColor: DesignKitColorName.contentSoft600.color, numberOfLines: 0, font: .body04Compact)
        return label
    }()
    
    private lazy var collectionView: UICollectionView = {
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: UICollectionViewFlowLayout())
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.contentInset = UIEdgeInsets(top: 32, left: 0, bottom: 0, right: 0)
        collectionView.backgroundColor = .clear
        return collectionView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            disabledTextColor: DesignKitColorName.contentSoft600.color,
            disabledBackgroundColor: DesignKitColorName.backgroundSub300.color,
            font: .bold03Compact,
            isEnabled: false
        )
        button.translatesAutoresizingMaskIntoConstraints = false
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

// MARK: - ExperienceCoachSelectedPresenterDelegate
extension ExperienceCoachSelectedViewController: ExperienceCoachSelectedPresenterDelegate {
    func prepareUI() {
        textStackView.addArrangedSubviews([titleLabel, descriptionLabel])
        view.addSubview(progressView)
        view.addSubview(textStackView)
        view.addSubview(continueButton)
        view.addSubview(collectionView)
        
        NSLayoutConstraint.activate([
            progressView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            progressView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            progressView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            
            textStackView.topAnchor.constraint(equalTo: progressView.bottomAnchor, constant: 41),
            textStackView.leadingAnchor.constraint(equalTo: progressView.leadingAnchor),
            textStackView.trailingAnchor.constraint(equalTo: progressView.trailingAnchor),
            
            continueButton.leadingAnchor.constraint(equalTo: progressView.leadingAnchor),
            continueButton.trailingAnchor.constraint(equalTo: progressView.trailingAnchor),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -50),
            continueButton.heightAnchor.constraint(equalToConstant: 46),
            
            collectionView.topAnchor.constraint(equalTo: textStackView.bottomAnchor),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: continueButton.topAnchor)
        ])
        
    }
    
    func didSetTitleAndDescription(title: String, description: String) {
        titleLabel.text = title
        descriptionLabel.text = description
    }
    
    func didSetContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func updateContinueButtonEnabled(_ isEnabled: Bool) {
        continueButton.setEnabled(isEnabled)
    }
}

// MARK: - UICollectionViewDataSource
extension ExperienceCoachSelectedViewController: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return presenter.model.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = RectangleImageTitleCollectionViewCell.dequeue(from: collectionView, at: indexPath)
        let model = presenter.model[indexPath.item]
        cell.bind(image: model.detail ?? "",
                  title: model.name ?? "",
                  isSelected: model.isSelected)
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        presenter.didSelectItemAt(indexPath)
    }
}

// MARK: - UICollectionViewDelegateFlowLayout
extension ExperienceCoachSelectedViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (collectionView.frame.width - 62) / 2,
                      height: 140)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 0, left: 24, bottom: 0, right: 24)
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 14
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 14
    }
}

extension ExperienceCoachSelectedViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedContinueButton()
    }
}
